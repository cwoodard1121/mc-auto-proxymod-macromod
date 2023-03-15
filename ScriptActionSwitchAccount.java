package com.itzblaze;

import com.mojang.authlib.Agent;
import com.mojang.authlib.AuthenticationService;
import com.mojang.authlib.UserAuthentication;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.util.UUIDTypeAdapter;
import net.eq2online.macros.scripting.api.*;
import net.eq2online.macros.scripting.parser.ScriptAction;
import net.eq2online.macros.scripting.parser.ScriptContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.util.Session;

import java.lang.reflect.Field;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.util.UUID;
@APIVersion(26)
public class ScriptActionSwitchAccount extends ScriptAction {
    boolean reconnect = false;
    UserAuthentication auth;
    public ScriptActionSwitchAccount() {
        super(ScriptContext.MAIN,"queueaccount");
    }

    @Override
    public boolean isThreadSafe() {
        return false;
    }
    public Authenticator getAuth(String user, String password) {
        return new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return (new PasswordAuthentication(user, password.toCharArray()));
            }
        };
    }
    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        String proxyAddress = provider.expand(macro,params[2],false);
        int proxyPort = Integer.parseInt(provider.expand(macro,params[3],false));
        String proxyUser = provider.expand(macro,params[4],false);
        String proxyPassword = provider.expand(macro,params[5],false);
        Authenticator.setDefault(getAuth(proxyUser,proxyPassword));
        /**
         * Setting up proxies...
         */
        InetSocketAddress address = new InetSocketAddress(proxyAddress,proxyPort);
        Proxy proxy = new Proxy(Proxy.Type.SOCKS,address);

        Minecraft mc = Minecraft.getMinecraft();
        AuthenticationService service = new YggdrasilAuthenticationService(proxy, UUID.randomUUID().toString());
        this.auth = service.createUserAuthentication(Agent.MINECRAFT);
        service.createMinecraftSessionService();

        try {
            setUser(provider.expand(macro,params[0],false),provider.expand(macro,params[1],false));
        } catch (Exception e) {
            e.printStackTrace();
            return new ReturnValue(-1);
        }
        return new ReturnValue(1);
    }
    private void setSession(Session s) throws Exception {
        Class<? extends Minecraft> mc = Minecraft.getMinecraft().getClass();
        try {
            Field session = null;

            for (Field f : mc.getDeclaredFields()) {
                if (f.getType().isInstance(s)) {
                    session = f;
                }
            }

            if (session == null) {
                throw new IllegalStateException("field not declared");
            }

            session.setAccessible(true);
            session.set(Minecraft.getMinecraft(), s);
            session.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    private void setUser(String email, String password) {
        Minecraft mc = Minecraft.getMinecraft();
        this.auth.logOut();
        this.auth.setUsername(email);
        this.auth.setPassword(password);

        try {
            this.auth.logIn();
            Session s = new Session(this.auth.getSelectedProfile().getName(), UUIDTypeAdapter.fromUUID(this.auth.getSelectedProfile().getId()),this.auth.getAuthenticatedToken(),this.auth.getUserType().getName());
            setSession(s);
        } catch(AuthenticationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onInit() {
        context.getCore().registerScriptAction(this);
    }
}
