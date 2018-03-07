package com.logic.transfer;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class DownloadReceive {
    Socket socket;

    public void start(int port, HttpServletResponse response) {
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(port));
            new Thread(new DownloadReceiveHandler(socket, response)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
