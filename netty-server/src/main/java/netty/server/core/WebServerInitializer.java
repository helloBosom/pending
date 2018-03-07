package netty.server.core;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.nio.charset.Charset;

/**
 * Web服务初始化
 */
class WebServerInitializer extends ChannelInitializer<SocketChannel> {

    protected void initChannel(final SocketChannel ch) {
        ch.pipeline()
                .addLast(new HttpServerCodec())
                .addLast(new HttpObjectAggregator(Integer.MAX_VALUE))
                .addLast(new ChunkedWriteHandler())
                .addLast(new WebServerHandler())
                .addLast(new StringEncoder(Charset.forName("UTF-8")))
                .addLast(new StringDecoder(Charset.forName("UTF-8")));
    }
}