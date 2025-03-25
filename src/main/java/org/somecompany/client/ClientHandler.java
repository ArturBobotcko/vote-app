package org.somecompany.client;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelPromise;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ClientHandler extends SimpleChannelInboundHandler<String> {
    private ChannelHandlerContext ctx;
    private BufferedReader clientCommandReader;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        this.ctx = ctx;
        this.clientCommandReader = new BufferedReader(new InputStreamReader(System.in));
        
        // Start reading input and sending to server in a separate thread
        new Thread(this::sendToServer).start();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) {
        System.out.println("Received: " + msg);
    }

    private void sendToServer() {
        try {
            System.out.println("Type messages to send to server (press Enter after each message):");
            while(true) {
                String command = clientCommandReader.readLine();
                if (command != null && ctx.channel().isActive()) {
                    ctx.writeAndFlush(command + "\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}