package org.somecompany.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.somecompany.commands.auth.LoginCommand;
import org.somecompany.commands.CommandRegister;
import org.somecompany.server.ServerHandler;

import io.netty.channel.embedded.EmbeddedChannel;

public class CommandRegisterTest {
    private ServerHandler serverHandler;
    private CommandRegister commandRegister;
    private EmbeddedChannel channel;

    /**
     * Setup the server handler and command register
     */
    @BeforeEach
    public void setUp() {
        serverHandler = new ServerHandler();
        commandRegister = new CommandRegister();
        channel = new EmbeddedChannel(serverHandler);
    }

    /**
     * Test the register command
     */
    @Test
    public void testRegisterCommand() {
        LoginCommand loginCommand = new LoginCommand(serverHandler);
        
        commandRegister.register(loginCommand);
        
        assertEquals(1, commandRegister.getCommands().size());
    }

    /**
     * Test command exection with no context provided
     */
    @Test
    public void testExecuteCommandNullContext() {
        LoginCommand loginCommand = new LoginCommand(serverHandler);
        
        commandRegister.register(loginCommand);

        // Assert that a NullPointerException is thrown when context is null
        assertThrows(NullPointerException.class, () -> {
            commandRegister.execute(null, "login", new String[] {"-u", "testuser"});
        });
    }
}
