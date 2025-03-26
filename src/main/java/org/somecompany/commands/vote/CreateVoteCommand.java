package org.somecompany.commands.vote;

import org.somecompany.commands.BaseCommand;
import io.netty.channel.ChannelHandlerContext;
import org.somecompany.server.ServerHandler;

public class CreateVoteCommand extends BaseCommand {
    private final ServerHandler serverHandler;

    public CreateVoteCommand(ServerHandler serverHandler) {
        this.serverHandler = serverHandler;
    }

    @Override
    protected Object createCommandInstance(ChannelHandlerContext ctx) {
        return new CreateVoteCommandCli(ctx, serverHandler);
    }
    
    @Override
    public Class<?> getCommandClass() {
        return CreateVoteCommandCli.class;
    }
}