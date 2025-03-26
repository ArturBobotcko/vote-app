package org.somecompany.server;

import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelFutureListener;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.somecompany.exceptions.UsernameTakenException;
import org.somecompany.client.Client;

import picocli.CommandLine;
import org.somecompany.commands.CommandRegister;
import org.somecompany.commands.auth.LoginCommand;

/**
 * @Sharable means that handler supports multiple connections
 */
@Sharable
public class ServerHandler extends SimpleChannelInboundHandler<String> {
    private final Map<Channel, Client> clients = new ConcurrentHashMap<>();
    private final CommandRegister commandRegister = new CommandRegister();

    /**
     * Register every command
     */
    public ServerHandler() {
        commandRegister.register(new LoginCommand(this));
    }

    /**
     * Fires when client's connection is established
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("Client connected: " + ctx.channel().remoteAddress());

        ctx.writeAndFlush("You are connected!\n");
        ctx.writeAndFlush("Type messages to send to server (press Enter after each message):\n");
        clients.put(ctx.channel(), new Client());
    }
    
    /**
     * Fires when client disconnects from server
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("Client disconnected: " + ctx.channel().remoteAddress());

        handleDisconnect(ctx);
    }

    private void handleDisconnect(ChannelHandlerContext ctx) {
        clients.remove(ctx.channel());
        ctx.writeAndFlush("You have been disconnected\n")
            .addListener(ChannelFutureListener.CLOSE);
        ctx.close();
    }
    
    /**
     * Called when server receive a message from client
     */
    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) {
        if (msg.equals("exit")) {
            handleDisconnect(ctx);
            return;
        }
        
        String[] commandParsed = msg.split("\\s+");
        String[] args = new String[commandParsed.length - 1];
        System.arraycopy(commandParsed, 1, args, 0, commandParsed.length - 1);

        commandRegister.execute(ctx, commandParsed[0], args);
        
        System.out.println("Message from " + ctx.channel().remoteAddress() + " (" + clients.get(ctx.channel()).getUsername() + "): " + msg);
    }

    /**
     * Handle login logic: search if user name is taken
     */
    public void handleLogin(Channel channel, String username) throws UsernameTakenException {
        for (Client client : clients.values()) {
            if (client.getUsername().equals(username)) {
                throw new UsernameTakenException("This username is already taken");
            }
        }
        
        Client client = clients.get(channel);
        client.login(username);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    public Map<Channel,Client> getClients() {
        return clients;
    }
}