package org.somecompany.server;

import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;

import java.util.HashSet;
import java.util.Set;

@Sharable
public class ServerHandler extends SimpleChannelInboundHandler<String> {
    private static final Set<Channel> channels = new HashSet<>();
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("Client connected: " + ctx.channel().remoteAddress());
        channels.add(ctx.channel());
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("Client disconnected: " + ctx.channel().remoteAddress());
        channels.remove(ctx.channel());
    }
    
    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) {
        System.out.println("Message from " + ctx.channel().remoteAddress() + ": " + msg);
        
        // Echo back to the client
        ctx.writeAndFlush("Server received: " + msg + "\n");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}