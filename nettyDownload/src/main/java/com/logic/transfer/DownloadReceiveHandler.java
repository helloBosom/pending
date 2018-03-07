package com.logic.transfer;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.Socket;

public class DownloadReceiveHandler implements Runnable {
    Socket socket;
    InputStream inputStream;
    HttpServletResponse response;
    OutputStream outputStream;

    public DownloadReceiveHandler(Socket socket, HttpServletResponse response) {
        this.socket = socket;
        this.response = response;
    }

    public void run() {
        try {
            inputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            outputStream = new BufferedOutputStream(response.getOutputStream());
            byte[] bytes = new byte[2048];
            int temp;
            while ((temp = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, temp);
            }
            outputStream.flush();
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}