package sqlite.src;

import model.Measurement;
import model.MeasurementInterval;

import java.sql.*;
import java.util.ArrayList;

public class SQLiteManager {

    private static final String LATEST_MEASUREMENT_SQL =
            "SELECT measures.rtime, sensors.name, metrics.metric_name, measures.rvalue, units.unit_name " +
            "FROM measures " +
            "INNER JOIN metrics, sensors, units " +
            "ON measures.metric_id = metrics.metric_id AND " +
            "measures.sensor_id = sensors.sensor_id AND " +
            "metrics.unit_id = units.unit_id " +
            "GROUP BY sensors.name, metrics.metric_name " +
            "HAVING MAX(measures.rtime)";

    private static final String STATS_BY_DATE_SQL =
            "SELECT measures.rtime, sensors.name, metrics.metric_name, MIN(measures.rvalue) AS minv, MAX(measures.rvalue) AS maxv, units.unit_name " +
            "FROM measures " +
            "INNER JOIN metrics, sensors, units " +
            "ON measures.metric_id = metrics.metric_id AND " +
            "measures.sensor_id = sensors.sensor_id AND " +
            "metrics.unit_id = units.unit_id " +
            "GROUP BY sensors.name, metrics.metric_name, SUBSTR(measures.rtime, 0, 11) " +
            "HAVING SUBSTR(measures.rtime, 0, 11) = ?";

    private Connection conn;

    public void connect() throws SQLException {
        String url = "jdbc:sqlite:src/sqlite/db/aranet.db";

        conn = DriverManager.getConnection(url);
//        System.out.println("Connection to SQLite has been established."); TODO to log
    }

    public void disconnect() throws SQLException {
        if (conn != null) {
            conn.close();
        }
    }

    public ArrayList<Measurement> getLatestMeasurement() {
        ArrayList<Measurement> sensors = new ArrayList<>();
        String sql = LATEST_MEASUREMENT_SQL;

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                sensors.add(new Measurement(
                        rs.getString("rtime"),
                        rs.getString("name"),
                        rs.getString("metric_name"),
                        rs.getString("unit_name"),
                        rs.getDouble("rvalue")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return sensors;
    }

    public ArrayList<MeasurementInterval> getStatsByDate(String date) {
        ArrayList<MeasurementInterval> sensors = new ArrayList<>();
        String sql = STATS_BY_DATE_SQL;

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, date);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                sensors.add(new MeasurementInterval(
                        rs.getString("rtime"),
                        rs.getString("name"),
                        rs.getString("metric_name"),
                        rs.getString("unit_name"),
                        rs.getDouble("minv"),
                        rs.getDouble("maxv")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return sensors;
    }

}
