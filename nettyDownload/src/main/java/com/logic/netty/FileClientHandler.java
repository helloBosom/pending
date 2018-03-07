package com.logic.netty;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.File;
import java.io.RandomAccessFile;

public class FileClientHandler extends ChannelInboundHandlerAdapter {

    private TransferFile transferFile;
    private int byteRead;
    private volatile int start = 0;

    FileClientHandler(TransferFile tf) {
        int i = 0;
//        while (tf.getFile().exists()) {
//            tf.setFileName(tf.getFileName() + "(" + ++i + ")");
//        }
//        tf.setFile(new File(tf.getFileName()));
//        this.transferFile = tf;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof TransferFile) {
            TransferFile transferFile = (TransferFile) msg;
            byte[] bytes = transferFile.getBytes();
            byteRead = transferFile.getEndPos();
            String fileName = transferFile.getFileName();//文件名
            File file = new File(transferFile.getLocation() + "/" + fileName);
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.seek(start);
            randomAccessFile.write(bytes);
            start = start + byteRead;
            if (byteRead > 0) {
                ctx.writeAndFlush(start);
                randomAccessFile.close();
            } else {
                ctx.close();
            }
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // TODO Auto-generated method stub
        super.channelInactive(ctx);
        System.out.println("hah");
        ctx.flush();
        ctx.close();
    }

}
