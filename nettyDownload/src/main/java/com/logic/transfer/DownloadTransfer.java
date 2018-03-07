package com.logic.transfer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class DownloadTransfer {
    ServerSocket serverSocket;
    Socket socket;

    public void start(int port, String fileLocation) {
        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                socket = serverSocket.accept();
                new Thread(new DownloadTransferHandler(socket, fileLocation)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
