package org.somecompany.commands;

import io.netty.channel.ChannelHandlerContext;
import java.util.HashMap;
import java.util.Map;
import java.io.StringWriter;
import java.io.PrintWriter;

import picocli.CommandLine;
import org.somecompany.commands.CommandInterface;
import org.somecompany.exceptions.UnknownCommandException;


/**
 * A class that register commands and execute them depends on their names
 */
public class CommandRegister {
    private final Map<String, CommandInterface> commands = new HashMap<>();

    /**
     * Register a command with provided name and Command oject
     */
    public void register(CommandInterface command) {
        Class<?> commandClass = command.getCommandClass();
        CommandLine.Command annotation = commandClass
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
    public void execute(ChannelHandlerContext ctx, String commandName, String[] args) throws UnknownCommandException {
        CommandInterface command = commands.get(commandName);
        if (command == null) {
            throw new UnknownCommandException("Unknown command: " + commandName);
        }
        command.execute(ctx, args);
    }

    /**
     * Get the map of commands
     */
    public Map<String, CommandInterface> getCommands() {
        return commands;
    }
}