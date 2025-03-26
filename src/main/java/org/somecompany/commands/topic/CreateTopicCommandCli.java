package org.somecompany.commands.topic;

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
@Command(
    name = "create topic",
    description = "Creation of a topic",
    mixinStandardHelpOptions = true
)
public class CreateTopicCommandCli implements Runnable {
    @Option(
        names = "-n",
        description = "Name of the topic",
        required = true
    )
    private String topicName;
    
    private ChannelHandlerContext ctx;
    private ServerHandler serverHandler;
    
    public CreateTopicCommandCli(ChannelHandlerContext ctx, ServerHandler serverHandler) {
        this.ctx = ctx;
        this.serverHandler = serverHandler;
    }

    @Override
    public void run() {
        System.out.println("CREATED TOPIC by " + serverHandler.getCurrentUsername(ctx));
        // TODO: save it to JSON
    }

    public String getTopicName() {
        return topicName;
    }
}