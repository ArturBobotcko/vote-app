package org.somecompany.client;

import io.netty.channel.embedded.EmbeddedChannel;
import org.somecompany.server.ServerHandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.channel.ChannelFutureListener;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Queue;

public class ClientHandlerTest {
    private EmbeddedChannel channel;
    private ClientHandler clientHandler;
    private InputStream originalSystemIn;

    @BeforeEach
    public void setup() {
        originalSystemIn = System.in;
        clientHandler = new ClientHandler();
        channel = new EmbeddedChannel(clientHandler);
    }
    
    @AfterEach
    public void cleanup() {
        System.setIn(originalSystemIn);
        channel.close();
    }

    @Test
    public void connectionEstablishedTest() {
        assertTrue(channel.writeOutbound("You are connected!\n"));

        String msg = channel.readOutbound();

        assertEquals("You are connected!\n", msg);
        assertEquals(true, channel.isActive());
    }

    @Test
    public void testChannelRead0() {
        String testMessage = "Test message from server";
        
        assertTrue(channel.writeOutbound(testMessage));
        
        assertNull(channel.readInbound());
    }

    @Test
    public void testSendToServer() throws Exception {
        String simulatedInput = "test command\n";
        ByteArrayInputStream testIn = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(testIn);
        
        channel.pipeline().fireChannelActive();
        
        Thread.sleep(500);
        
        String outboundMsg = channel.readOutbound();
        assertEquals("test command\n", outboundMsg);
    }
}