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

import java.net.InetSocketAddress;

public class Server {
    private final int port;

    public Server(final int port) {
        this.port = port;
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: " + Server.class.getSimpleName() + " <port>");
            return;
        }

        try {
            int port = Integer.parseInt(args[0]);
            new Server(port).start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Start a server
     */
    public void start() throws Exception {
        EventLoopGroup serverGroup = new NioEventLoopGroup(1);
        EventLoopGroup clients = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(serverGroup, clients)

             // Server socket
             .channel(NioServerSocketChannel.class)

             // Server address with provided port
             .localAddress(new InetSocketAddress(port))
             .handler(new LoggingHandler(LogLevel.INFO))
             .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline p = ch.pipeline();
                    p.addLast(new LineBasedFrameDecoder(1024))
                     .addLast(new StringDecoder())
                     .addLast(new StringEncoder())
                     .addLast(new ServerHandler());
                     
                }
             });

            System.out.println("Server started on port " + port);
            ChannelFuture f = b.bind(port).sync();

            f.channel().closeFuture().sync();
        } finally {
            serverGroup.shutdownGracefully();
            clients.shutdownGracefully();
        }
    }
}