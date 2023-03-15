package com.itzblaze;


public class Proxy {
    public boolean enabled;
    public String ip = "";
    public int port = 0;
    public String userID = "";
    public String username = "";
    public String password = "";

    public Proxy() {
        this.enabled = false;
    }

    public Proxy(String ip, int port, String username, String password) {
        this.enabled = !ip.isEmpty();
        this.ip = ip;
        this.port = port;
        this.userID = userID;
        this.username = username;
        this.password = password;
    }
}