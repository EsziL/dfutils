package com.eszil.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;

import java.util.regex.Pattern;

public class ServerUtils {

    public static boolean isOnDF() {
        ServerInfo server = MinecraftClient.getInstance().getCurrentServerEntry();
        if (server != null) return Pattern.compile("mcdiamondfire.com").matcher(server.address).find();
        else return false;
    }
}
