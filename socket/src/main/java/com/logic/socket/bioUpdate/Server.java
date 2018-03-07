package com.logic.socket.bioUpdate;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    final static int PORT = 8765;

    public static void main(String[] args) {
        ServerSocket serverSocket;
        Socket socket;
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("server start");
            //内部封装了 一个ThreadPoolExecutor对象
            HandlerExecutorPool executorPool = new HandlerExecutorPool(50, 1000);
            //使用线程池节省线程的创建与销毁带来的资源浪费。
            while (true) {
                socket = serverSocket.accept();
                executorPool.execute(new ServerHandler(socket));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
