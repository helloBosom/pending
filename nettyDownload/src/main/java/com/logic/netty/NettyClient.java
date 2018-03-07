package com.logic.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NettyClient {
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    EventLoopGroup group = new NioEventLoopGroup();
    TransferFile transferFile = new TransferFile();

    public void connect(final int port, final String host) throws InterruptedException {
        transferFile.setLocation("");
        transferFile.setFileName("");
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyMessageDecoder(1024 * 1024, 4, 4));
                            ch.pipeline().addLast("MessageEncoder", new NettyMessageEncoder());
                            ch.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(50));
                            ch.pipeline().addLast("LoginAuthHandler", new LoginAuthReqHandler());
                            ch.pipeline().addLast("HearBeatHandler", new HeartBeatReqHandler());
                            ch.pipeline().addLast("FileClientHandler", new FileClientHandler(transferFile));
                        }
                    });
            ChannelFuture future = null;
            try {
                future = bootstrap.connect(new InetSocketAddress(host, port),
                        new InetSocketAddress(getWindowsIp.getLocalIp(), 5000)).sync();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            future.channel().closeFuture().sync();
        } finally {
            executor.execute(() -> {
                try {
                    TimeUnit.SECONDS.sleep(5);
                    try {
                        connect(port, host);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new NettyClient().connect(9000, "192.168.74.128");
    }

}
