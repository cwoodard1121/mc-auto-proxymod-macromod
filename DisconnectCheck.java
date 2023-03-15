package com.itzblaze;

import com.itzblaze.Cache;
import com.mumfrey.liteloader.GameLoopListener;
import com.mumfrey.liteloader.client.LiteLoaderEventBrokerClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DisconnectCheck implements GameLoopListener {
    int ticks = 0;
    public static DisconnectCheck INSTANCE = new DisconnectCheck();
    public DisconnectCheck() {
        LiteLoaderEventBrokerClient.getInstance().addLoopListener(this);
    }
    @Override
    public void onRunGameLoop(Minecraft minecraft) {
        GuiScreen screen = minecraft.currentScreen;
        if(minecraft.currentScreen instanceof GuiDisconnected) {
            ticks++;
            if(Cache.ticks == ticks) {
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
                minecraft.displayGuiScreen(new GuiConnecting(screen, minecraft, new ServerData("hypixel", "mc.hypixel.net", false)));
                ticks = 0;
            }
        }
    }

    @Override
    public String getVersion() {
        return null;
    }

    @Override
    public void init(File configPath) {

    }

    @Override
    public void upgradeSettings(String version, File configPath, File oldConfigPath) {

    }

    @Override
    public String getName() {
        return null;
    }
}
