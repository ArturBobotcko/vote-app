package org.somecompany.server;

import io.netty.bootstrap.ServerBootstrap;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.codec.LineBasedFrameDecoder;

import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class Server {
    public void start(final int portNumber) throws Exception {
        EventLoopGroup serverGroup = new NioEventLoopGroup(1);
        EventLoopGroup clients = new NioEventLoopGroup();
        ServerHandler serverHandler = new ServerHandler();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(serverGroup, clients) 
             .channel(NioServerSocketChannel.class)
             .handler(new LoggingHandler(LogLevel.INFO))
             .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline p = ch.pipeline();
                    p.addLast(new LineBasedFrameDecoder(1024))
                     .addLast(new StringDecoder())
                     .addLast(new StringEncoder())
                     .addLast(serverHandler);
                     
                }
             });

            System.out.println("Server started on port " + portNumber);
            ChannelFuture f = b.bind(portNumber).sync();

            f.channel().closeFuture().sync();
        } finally {
            serverGroup.shutdownGracefully();
            clients.shutdownGracefully();
        }
    }
}