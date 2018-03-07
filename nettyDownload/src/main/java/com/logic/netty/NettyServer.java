package com.logic.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.io.File;
import java.net.UnknownHostException;

public class NettyServer {
    TransferFile transferFile = new TransferFile();

    public void bind(String ip, int port) throws InterruptedException {
        //transferFile.setLocation("");
        //transferFile.setFileName("");
        transferFile.setFile(new File("D:\\netty.txt"));
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 100).handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel ch) throws Exception {
                        //ch.pipeline().addLast(new NettyMessageDecoder(1024 * 1024, 4, 4));
                        //ch.pipeline().addLast(new NettyMessageEncoder());
                        //ch.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(50));
                        //ch.pipeline().addLast(new LoginAuthRespHandler());
                        // ch.pipeline().addLast("HeartBeatHandler", new HeartBeatRespHandler());
                        ch.pipeline().addLast("FileServerHandler", new FileServerHandler(transferFile));
                    }
                });
        bootstrap.bind(ip, port).sync();
        System.out.println("netty server start ok :" + ip + ":" + port);
    }

    public static void main(String[] args) throws InterruptedException, UnknownHostException {
        new NettyServer().bind(getWindowsIp.getLocalIp(), 5000);
    }

}
