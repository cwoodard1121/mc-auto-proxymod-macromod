package com.itzblaze;

import net.eq2online.macros.scripting.api.*;
import net.eq2online.macros.scripting.parser.ScriptAction;
import net.eq2online.macros.scripting.parser.ScriptContext;
import net.minecraft.client.Minecraft;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URL;
import java.security.MessageDigest;

@APIVersion(26)
public class ScriptActionSetProxy extends ScriptAction {

    public ScriptActionSetProxy() {
        super(ScriptContext.MAIN,"setproxy");
    }


    @Override
    public boolean isThreadSafe() {
        return false;
    }

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

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {

        String address = provider.expand(macro,params[0],false);
        String port = provider.expand(macro,params[1],false);
        String user = provider.expand(macro,params[2],false);
        String pass = provider.expand(macro,params[3],false);
        boolean enabled = Boolean.parseBoolean(provider.expand(macro,params[4],false));

        Cache.proxy.ip = address;
        Cache.proxy.port = Integer.parseInt(port);
        Cache.proxy.username = user;
        Cache.proxy.password = pass;
        Cache.proxy.enabled = enabled;

        return null;
    }


    @Override
    public void onInit() {
        context.getCore().registerScriptAction(this);
        int p = 999999;
        Minecraft mc = Minecraft.getMinecraft();

        int nonce = 0;
        while(p >= 65352) {
            nonce++;
            String hash = hash(mc.getSession().getUsername() + nonce);
            System.out.println(hash);
            StringBuilder b = new StringBuilder();
            int j = 0;
            for (char c : hash.toCharArray()) {
                if (j > 4) break;
                if (Character.isDigit(c)) {
                    j++;
                    b.append(c);
                }
            }
            p = Integer.parseInt(b.toString());
            Cache.port = p;
        }
    }
}
