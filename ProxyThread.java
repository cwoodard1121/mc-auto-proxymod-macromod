package com.itzblaze;

import net.minecraft.client.Minecraft;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;

public class ProxyThread {

    private int port = 999999;
    private Socket s;

    private String hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(input.getBytes("UTF-8"));

            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void start() throws IOException {
        Minecraft mc = Minecraft.getMinecraft();
        int nonce = 0;
        while(port >= 65352) {
            nonce++;
            String hash = hash(mc.getSession().getUsername() + nonce);
            System.out.println(hash);
            StringBuilder b = new StringBuilder();
            int j = 0;
            for (char c : hash.toCharArray()) {
                if (j > 5) break;
                if (Character.isDigit(c)) {
                    j++;
                    b.append(c);
                }
            }
            port = Integer.parseInt(b.toString());
        }
        s = new Socket(InetAddress.getLocalHost().getHostAddress(),port);
    }

    public void send(String msg) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
        out.writeObject(msg);
        out.close();
    }


}
