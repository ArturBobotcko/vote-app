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

import java.net.InetSocketAddress;

public class ClientSocket {
    private final String host;
    private final int port;

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: " + ClientSocket.class.getSimpleName() + " <host> <port>");
            return;
        }

        try {
            String host = args[0];
            int port = Integer.parseInt(args[1]);
            new ClientSocket(host, port).start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private ClientSocket(final String host, final int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Start a client and connect to the specified server
     */
    private void start() throws Exception {
        EventLoopGroup clientGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(clientGroup)
             .channel(NioSocketChannel.class)
             .remoteAddress(new InetSocketAddress(host, port))
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

            System.out.println("Connecting to server at " + host + ":" + port);
            
            // Connect to the server
            ChannelFuture f = b.connect().sync();
            System.out.println("Connected to server!");

            f.channel().closeFuture().sync();
        } finally {
            clientGroup.shutdownGracefully();
        }
    }
} 