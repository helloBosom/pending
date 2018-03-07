package com.logic.transfer;

import java.io.*;
import java.net.Socket;

public class DownloadTransferHandler implements Runnable {
    Socket socket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    String fileLocation;

    public DownloadTransferHandler(Socket socket, String fileLocation) {
        this.socket = socket;
        this.fileLocation = fileLocation;
    }

    public void run() {
        try {
            dataInputStream = new DataInputStream(new FileInputStream(new File(fileLocation)));
            dataOutputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            byte[] bytes = new byte[2048];
            int temp;
            while ((temp = dataInputStream.read(bytes)) != -1) {
                dataOutputStream.write(bytes, 0, temp);
                dataOutputStream.flush();
            }
            if (dataInputStream != null) {
                dataInputStream.close();
            }
            if (dataOutputStream != null) {
                dataOutputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
