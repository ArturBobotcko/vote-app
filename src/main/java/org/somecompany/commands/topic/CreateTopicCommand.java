package org.somecompany.commands.topic;

import org.somecompany.commands.BaseCommand;
import io.netty.channel.ChannelHandlerContext;
import org.somecompany.server.ServerHandler;

public class CreateTopicCommand extends BaseCommand {
    private final ServerHandler serverHandler;

    public CreateTopicCommand(ServerHandler serverHandler) {
        this.serverHandler = serverHandler;
    }

    @Override
    protected Object createCommandInstance(ChannelHandlerContext ctx) {
        return new CreateTopicCommandCli(ctx, serverHandler);
    }
    
    @Override
    public Class<?> getCommandClass() {
        return CreateTopicCommandCli.class;
    }
}