package com.logic.socket.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPReceiver {

    public static void main(String[] args) {
        try {
            /**
             * 此类表示用来发送和接收数据报包的套接字。
             */
            DatagramSocket receiveSocket = new DatagramSocket(10000);
            byte buf[] = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(buf, buf.length);
            System.out.println("开始接收数据报......");
            while (true) {
                receiveSocket.receive(receivePacket);
                String receiveData = new String(receivePacket.getData(), 0, receivePacket.getLength());
                String name = receivePacket.getAddress().getHostName();
                int port = receivePacket.getPort();
                System.out.println("来自主机：" + name + "，的端口：" + port + "的数据：" + receiveData);
                if ("Time".equals(receiveData)) {
                    byte[] timeMillis = String.valueOf(System.currentTimeMillis()).getBytes();
                    System.out.println("send msg : " + String.valueOf(System.currentTimeMillis()));
                    receiveSocket.send(new DatagramPacket(timeMillis, timeMillis.length, receivePacket.getAddress(), receivePacket.getPort()));
                }
            }
        } catch (SocketException e) {
            System.out.println("can not open the datagram socket");
            System.exit(1);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }
}