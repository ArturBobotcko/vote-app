package org.somecompany.commands.vote;

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
    name = "create vote",
    description = "Create vote in provided topic",
    mixinStandardHelpOptions = true
)
public class CreateVoteCommandCli implements Runnable {
    @Option(
        names = "-t",
        description = "Name of the topic",
        required = true
    )
    private String topicName;
    
    private ChannelHandlerContext ctx;
    private ServerHandler serverHandler;
    
    public CreateVoteCommandCli(ChannelHandlerContext ctx, ServerHandler serverHandler) {
        this.ctx = ctx;
        this.serverHandler = serverHandler;
    }

    @Override
    public void run() {
        System.out.println("CREATED VOTE");
    }

    public String getTopicName() {
        return topicName;
    }
}