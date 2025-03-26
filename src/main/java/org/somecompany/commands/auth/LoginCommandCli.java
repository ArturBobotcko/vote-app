package org.somecompany.commands.auth;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

import java.util.concurrent.Callable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.Channel;
import org.somecompany.server.ServerHandler;
import org.somecompany.exceptions.UsernameTakenException;

/**
 * Picocli-based command class.
 * 
 * Provides a 'login' command for client
 */
public class LoginCommandCli implements Runnable {
    @Option(
        names = "-u",
        description = "Username to log in with",
        required = true
    )
    private String username;
    
    private ChannelHandlerContext ctx;
    
    public LoginCommandCli(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public void run() {
        try {
            ServerHandler serverHandler = (ServerHandler) ctx.pipeline().get("serverHandler");
            serverHandler.handleLogin(ctx.channel(), username);
            ctx.writeAndFlush("Login successful as: " + username + "\n");
        } catch (UsernameTakenException e) {
            ctx.writeAndFlush("Login failed: " + e.getMessage() + "\n");
        } catch (Exception e) {
            ctx.writeAndFlush("Error during login: " + e.getMessage() + "\n");
        }
    }

    public String getUsername() {
        return username;
    }
}