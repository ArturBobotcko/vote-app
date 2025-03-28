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
import org.somecompany.commands.topic.CreateTopicCommand;
import org.somecompany.commands.vote.CreateVoteCommand;

import org.somecompany.util.LoggerUtil;

import org.somecompany.exceptions.UnknownCommandException;

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
        commandRegister.register(new CreateTopicCommand(this));
        commandRegister.register(new CreateVoteCommand(this));
    }

    /**
     * Fires when client's connection is established
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        LoggerUtil.info("Client connected: " + ctx.channel().remoteAddress());

        ctx.writeAndFlush("You are connected!\n");
        ctx.writeAndFlush("Type messages to send to server (press Enter after each message):\n");
        clients.put(ctx.channel(), new Client());
    }
    
    /**
     * Fires when client disconnects from server
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        LoggerUtil.info("Client disconnected: " + ctx.channel().remoteAddress());

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

        /**
         * Parse user input.
         * 
         * If not logged in, commands aren't available
         */
        if (getCurrentClient(ctx).isLoggedIn() || msg.contains("login")) {
            int dashIndex = msg.indexOf('-');
            String commandName;
            String[] commandArgs;

            if (dashIndex == -1) {  // Если нет флагов
                commandName = msg.trim();
                commandArgs = new String[0];  // Пустой массив аргументов
            } else {  // Если есть флаги
                commandName = msg.substring(0, dashIndex).trim();
                commandArgs = msg.substring(dashIndex).trim().split("\\s+");
            }

            try {
                commandRegister.execute(ctx, commandName, commandArgs);
                LoggerUtil.info("Message from " + ctx.channel().remoteAddress() + " (" + getCurrentUsername(ctx) + "): " + msg);
            } catch (UnknownCommandException e) {
                ctx.writeAndFlush(e.getMessage() + "\n");
                LoggerUtil.error(e.getMessage());
            }
        } else {
            ctx.writeAndFlush("You have to login first\n");
        }

        // System.out.println("Message from " + ctx.channel().remoteAddress() + " (" + getCurrentUsername(ctx) + "): " + msg);
        
    }

    /**
     * Get Client object by context
     */
    public Client getCurrentClient(ChannelHandlerContext ctx) {
        return clients.get(ctx.channel());
    }

    /**
     * Get Client username by context
     */
    public String getCurrentUsername(ChannelHandlerContext ctx) {
        return getCurrentClient(ctx).getUsername();
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