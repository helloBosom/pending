package com.logic.socket.nio;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private ExecutorService executorService;
    private AsynchronousChannelGroup threadGroup;
    public AsynchronousServerSocketChannel asynchronousServerSocketChannel;

    public Server(int port) {
        try {
            //创建一个缓存池
            executorService = Executors.newCachedThreadPool();
            //创建线程组
            threadGroup = AsynchronousChannelGroup.withCachedThreadPool(executorService, 1);
            //创建服务器通道
            asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open(threadGroup);
            //进行绑定
            asynchronousServerSocketChannel.bind(new InetSocketAddress(port));
            System.out.println("server start , port : " + port);
            while (true) {
                asynchronousServerSocketChannel.accept(this, new ServerCompletionHandler());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server(8765);
    }
}
