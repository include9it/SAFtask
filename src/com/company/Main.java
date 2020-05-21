package com.company;

import server.SimpleHttpServer;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            System.out.println("1");
            SimpleHttpServer.start();
            System.out.println("2");

        } catch (IOException throwables) {
            throwables.printStackTrace();
        }
    }

}
