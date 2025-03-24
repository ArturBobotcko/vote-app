package org.somecompany.server;

public class StartServer {
    public final static int PORT_NUMBER = 8080;

    public static void main(String[] args) {    
        try {
            Server server = new Server();
            server.start(PORT_NUMBER);
        } catch (Exception e) {
            System.out.println("Starting server error: " + e);
        }
    }
}