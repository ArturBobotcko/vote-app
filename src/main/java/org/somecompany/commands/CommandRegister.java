package org.somecompany.commands;

import io.netty.channel.ChannelHandlerContext;
import java.util.HashMap;
import java.util.Map;

import picocli.CommandLine;
import org.somecompany.commands.CommandInterface;


/**
 * A class that register commands and execute them depends on their names
 */
public class CommandRegister {
    private final Map<String, CommandInterface> commands = new HashMap<>();

    /**
     * Register a command with provided name and Command oject
     */
    public void register(CommandInterface command) {
        CommandLine.Command annotation = command.getClass()
            .getAnnotation(CommandLine.Command.class);
        
        if (annotation == null) {
            throw new IllegalArgumentException("Command class must have @Command annotation");
        }
        
        // Use name from annotation
        String name = annotation.name();
        commands.put(name, command);
    }

    /**
     * Execute a specified command with arguments
     */
    public void execute(ChannelHandlerContext ctx, String commandName, String[] args) {
        CommandInterface command = commands.get(commandName);
        if (command != null) {
            command.execute(ctx, args);
        } else {
            ctx.writeAndFlush("Unknown command: " + commandName + "\n");
        }
    }
}