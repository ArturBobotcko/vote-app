package org.somecompany.commands.auth;

import org.somecompany.commands.CommandInterface;
import io.netty.channel.ChannelHandlerContext;
import org.somecompany.server.ServerHandler;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(
    name = "login",
    description = "Log in to the server",
    mixinStandardHelpOptions = true
)
public class LoginCommand implements CommandInterface {
    private final ServerHandler serverHandler;

    public LoginCommand(ServerHandler serverHandler) {
        this.serverHandler = serverHandler;
    }

    @Override
    public void execute(ChannelHandlerContext ctx, String[] args) {
        new CommandLine(new LoginCommandCli(ctx, serverHandler)).execute(args);
    }
}