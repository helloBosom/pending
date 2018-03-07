package com.logic.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileServerHandler extends ChannelInboundHandlerAdapter {
    private int byteRead;
    private volatile int start = 0;
    private volatile int lastLength = 0;
    public RandomAccessFile randomAccessFile;
    private TransferFile transferFile;

    public FileServerHandler(TransferFile tf) {
//        if (tf.getFile().exists()) {
//            if (!tf.getFile().isFile()) {
//                System.out.println("Not a file :" + tf.getFile());
//                return;
//            }
//        }
        this.transferFile = tf;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("hah1");
        if (msg instanceof Integer) {
            start = (Integer) msg;
            if (start != -1) {
                randomAccessFile = new RandomAccessFile(
                        transferFile.getFile(), "r");
                randomAccessFile.seek(start);
                int a = (int) (randomAccessFile.length() - start);
                if (a < lastLength) {
                    lastLength = a;
                }
                byte[] bytes = new byte[lastLength];
                if ((byteRead = randomAccessFile.read(bytes)) != -1
                        && (randomAccessFile.length() - start) > 0) {
                    transferFile.setEndPos(byteRead);
                    transferFile.setBytes(bytes);
                    try {
                        ctx.writeAndFlush(transferFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    randomAccessFile.close();
                    ctx.close();
                }
            }
        }
        System.out.println("hah2");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush("hah");
        try {
            randomAccessFile = new RandomAccessFile(transferFile.getFile(),
                    "rw");
            randomAccessFile.seek(transferFile.getStartPos());
            // lastLength = (int) randomAccessFile.length() / 10;
            lastLength = 1;
            byte[] bytes = new byte[lastLength];
            if ((byteRead = randomAccessFile.read(bytes)) != -1) {
                transferFile.setEndPos(byteRead);
                transferFile.setBytes(bytes);
                ctx.writeAndFlush(transferFile);
                System.out.println("hah4");
            } else {
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException i) {
            i.printStackTrace();
        }
        System.out.println("hah3");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
