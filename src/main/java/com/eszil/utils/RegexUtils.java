package com.eszil.utils;

import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class RegexUtils {

    public static boolean didJoinGame(Text message) {
        return didJoinPlay(message) || didJoinDev(message) || didJoinBuild(message);
    }

    public static boolean didJoinDev(Text message) {
        return Pattern.compile("^» You are now in dev mode\\.$").matcher(message.getString()).find();
    }

    public static boolean didJoinBuild(Text message) {
        return Pattern.compile("^» You are now in build mode\\.$").matcher(message.getString()).find();
    }

    public static boolean didJoinPlay(Text message) {
        return Pattern.compile("^» Joined game: (.+?) by ([^.]+)\\.$").matcher(message.getString()).find();
    }
}
