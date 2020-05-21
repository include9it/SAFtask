package com.company;

import server.SimpleHttpServer;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            SimpleHttpServer.start();
        } catch (IOException throwables) {
            throwables.printStackTrace();
        }
    }

}
