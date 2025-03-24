package org.somecompany.client;

import org.somecompany.server.StartServer;

public class StartClient {
    public static void main(String[] args) {
        
        try {
            Client client = new Client();
            client.start("localhost", StartServer.PORT_NUMBER);
        } catch (Exception e) {
            System.out.println("Starting server error: " + e);
        }
    }
}