package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import model.AbstractInfo;
import sqlite.src.SQLiteManager;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SimpleHttpServer {

    public static void start() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/saftehnikatask", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    static class MyHandler implements HttpHandler {
        private static final String INTERNAL_SERVER_ERROR = "Internal Server Error";
        private final SQLiteManager sqLiteManager = new SQLiteManager();
        private Map<String, String> params;

        public void handle(HttpExchange t) throws IOException {
            try {
                sqLiteManager.connect();

                String statsResponse = "";
                if (t.getRequestURI().getQuery() != null) {//TODO make separate method
                    params = queryToMap(t.getRequestURI().getQuery());
//                    System.out.println("param date=" + params.get("date"));//TODO to log
                    statsResponse = ",\n\"stats\": " + getJson(sqLiteManager.getStatsByDate(params.get("date")));
                }

                String response = "{\"latest\": "
                        + getJson(sqLiteManager.getLatestMeasurement())
                        + statsResponse
                        + "}\n";
                t.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
                t.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();

                sqLiteManager.disconnect();

            } catch (SQLException ignore) {
                OutputStream os = t.getResponseBody();
                os.write(INTERNAL_SERVER_ERROR.getBytes());
                os.close();
            }
        }
    }

    public static Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            } else {
                result.put(entry[0], "");
            }
        }
        return result;
    }

    public static <T extends AbstractInfo> String getJson(ArrayList<T> obj) {
        return new Gson().toJson(obj);
    }
}
