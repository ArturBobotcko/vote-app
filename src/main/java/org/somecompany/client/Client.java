package org.somecompany.client;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Client {
    private String username;
    private boolean loggedIn;

    public Client() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
        this.username = "temp_" + now.format(formatter);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public void login(String username) {
        if (loggedIn) return;
        
        setUsername(username);
        setLoggedIn(true);
    }
}