package org.somecompany.commands;

import io.netty.channel.ChannelHandlerContext;

/**
 * An interface that every command class should implement.
 * 
 * An implementation should include this:
 * new CommandLine(new <picocli-based command object>).execute(args);
 */
public interface CommandInterface {
    void execute(ChannelHandlerContext ctx, String[] args);
}