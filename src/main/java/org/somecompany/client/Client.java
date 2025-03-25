package org.somecompany.client;

import io.netty.bootstrap.Bootstrap;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.codec.LineBasedFrameDecoder;

public class Client {
    public void start(String host, final int portNumber) throws Exception {
        EventLoopGroup clientGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(clientGroup)
             .channel(NioSocketChannel.class)
             .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline p = ch.pipeline();
                    p.addLast(new LineBasedFrameDecoder(1024))
                     .addLast(new StringDecoder())
                     .addLast(new StringEncoder())
                     .addLast(new ClientHandler());
                }
             });

            System.out.println("Connecting to server at " + host + ":" + portNumber);
            ChannelFuture f = b.connect(host, portNumber).sync();
            System.out.println("Connected to server!");

            f.channel().closeFuture().sync();
        } finally {
            clientGroup.shutdownGracefully();
        }
    }
}