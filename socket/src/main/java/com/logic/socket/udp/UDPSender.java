package com.logic.socket.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UDPSender {

    public static void main(String[] args) {
        try {
            DatagramSocket socket = new DatagramSocket();
            String string = "Time";
            byte[] dataByte = string.getBytes();
            DatagramPacket packet = new DatagramPacket(dataByte,
                    dataByte.length, InetAddress.getByName("127.0.0.1"), 10000);
            socket.send(packet);
            System.out.println("send the data:" + string);

            DatagramPacket receiveDp = new DatagramPacket(new byte[1024], 1024);
            socket.receive(receiveDp);
            String response = new String(receiveDp.getData(), 0, receiveDp.getLength());
            String received = "服务器时间为:  " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.valueOf(response)));
            System.out.println(received);
        } catch (SocketException e) {
            System.out.println("can not open the gatagramSocket");
            System.exit(1);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }
}