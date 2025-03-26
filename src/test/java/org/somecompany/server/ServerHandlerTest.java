package org.somecompany.server;

import io.netty.channel.embedded.EmbeddedChannel;
import org.somecompany.server.ServerHandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import io.netty.channel.ChannelFutureListener;

import java.util.Queue;

public class ServerHandlerTest {
    private EmbeddedChannel channel;
    private ServerHandler serverHandler;

    @BeforeEach
    public void setup() {
        serverHandler = new ServerHandler();
        channel = new EmbeddedChannel(serverHandler);
    }

    /**
     * Client connected to server
     */
    @Test
    public void clientConnectedTest() {
        String msg = channel.readOutbound();

        assertEquals(true, channel.isActive());
        assertEquals(1, serverHandler.getClients().size());
        assertEquals("You are connected!\n", msg);

        msg = channel.readOutbound();
        assertEquals("Type messages to send to server (press Enter after each message):\n", msg);
    }

    /**
     * Client disconnected from server
     */
    @Test
    public void clientDisconnectedTest() {
        channel.outboundMessages().clear();

        channel.writeInbound("exit");

        String msg = channel.readOutbound();

        assertEquals(false, channel.isActive());
        assertEquals(0, serverHandler.getClients().size());
        assertEquals("You have been disconnected\n", msg);
    }

    /**
     * Client logged in
     */
    @Test
    public void clientLoggedInTest() {
        channel.outboundMessages().clear();

        channel.writeInbound("login -u=username1");

        String msg = channel.readOutbound();

        assertEquals("Login successful as: username1\n", msg);
    }
}