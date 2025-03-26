package org.somecompany.commands.auth;

import org.somecompany.commands.BaseCommand;
import io.netty.channel.ChannelHandlerContext;
import org.somecompany.server.ServerHandler;

public class LoginCommand extends BaseCommand {
    private final ServerHandler serverHandler;

    public LoginCommand(ServerHandler serverHandler) {
        this.serverHandler = serverHandler;
    }

    @Override
    protected Object createCommandInstance(ChannelHandlerContext ctx) {
        return new LoginCommandCli(ctx, serverHandler);
    }
    
    @Override
    public Class<?> getCommandClass() {
        return LoginCommandCli.class;
    }
}