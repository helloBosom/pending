package com.logic.socket.bio;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    final static int PORT = 8765;

    public static void main(String[] args) {
        ServerSocket serverSocket;
        Socket socket;
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println(" server start .. ");
            while (true) {
                socket = serverSocket.accept();
                new Thread(new ServerHandler(socket)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
