package com.itzblaze;

import net.eq2online.macros.scripting.api.*;
import net.eq2online.macros.scripting.parser.ScriptAction;
import net.eq2online.macros.scripting.parser.ScriptContext;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@APIVersion(26)
public class ScriptActionConnect extends ScriptAction {
    public ScriptActionConnect() {
        super(ScriptContext.MAIN,"connect");
    }

    @Override
    public boolean isThreadSafe() {
        return false;
    }


    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        try {
            ServerData hp = new ServerData("server", provider.expand(macro, params[0], true), false);
            assert mc.currentScreen != null;
            URL url = null;
            try {
                url = new URL("http://localhost:" + Cache.port + "/proxies/?address=" + Cache.proxy.ip + "&user=" + Cache.proxy.username + "&pass=" + Cache.proxy.password + "&port=" + Cache.proxy.port);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection con = null;
            try {
                con = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                con.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mc.displayGuiScreen(new GuiConnecting(mc.currentScreen, mc, hp));
            return new ReturnValue(true);
        } catch (Exception e) {

        }
        return new ReturnValue(false);
    }

    @Override
    public void onInit() {
        context.getCore().registerScriptAction(this);
    }
}
