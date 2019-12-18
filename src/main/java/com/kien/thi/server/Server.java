package com.kien.thi.server;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    ServerSocket serverSocket;

    public Server() {
        try {
            serverSocket = new ServerSocket(5000);
        } catch (Exception ex) {
            System.err.println("Error init " + ex.getMessage());
        }
    }

    public void go() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                new ClientHandler(socket).start();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new Server().go();
    }
}

