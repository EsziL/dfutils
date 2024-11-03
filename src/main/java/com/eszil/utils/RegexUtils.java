package com.eszil.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class RegexUtils {

    public static boolean didJoinGame(Text message) {
        String messageAsString = message.getString();

        boolean joinedGame = Pattern.compile("^» Joined game: (.+?) by ([^.]+)\\.$").matcher(messageAsString).find();
        boolean joinedDev = Pattern.compile("^» You are now in dev mode\\.$").matcher(messageAsString).find();
        boolean joinedBuild = Pattern.compile("^» You are now in build mode\\.$").matcher(messageAsString).find();

        return joinedGame || joinedDev || joinedBuild;
    }

    public static boolean didJoinDev(Text message) {
        return Pattern.compile("^» You are now in dev mode\\.$").matcher(message.getString()).find();
    }
}
