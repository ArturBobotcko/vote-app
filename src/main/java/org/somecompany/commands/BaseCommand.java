package org.somecompany.commands;

import io.netty.channel.ChannelHandlerContext;
import picocli.CommandLine;
import java.io.StringWriter;
import java.io.PrintWriter;

/**
 * Abstract base class for command implementations.
 * Handles common functionality like output redirection.
 */
public abstract class BaseCommand implements CommandInterface {
    
    @Override
    public void execute(ChannelHandlerContext ctx, String[] args) {
        // Create command line with custom output streams to capture the output
        StringWriter out = new StringWriter();
        StringWriter err = new StringWriter();
        
        CommandLine cmd = new CommandLine(createCommandInstance(ctx));
        cmd.setOut(new PrintWriter(out));
        cmd.setErr(new PrintWriter(err));
        
        int exitCode = cmd.execute(args);
        
        // If there was output in either stream, send it to the client
        String output = out.toString();
        String error = err.toString();
        
        if (!output.isEmpty()) {
            ctx.writeAndFlush(output);
        }
        
        if (!error.isEmpty()) {
            ctx.writeAndFlush(error);
        }
    }
    
    /**
     * Create an instance of the Picocli command class.
     * Subclasses must implement this to provide their specific command instance.
     * 
     * @param ctx The ChannelHandlerContext to pass to the command
     * @return A new instance of the command
     */
    protected abstract Object createCommandInstance(ChannelHandlerContext ctx);
} 