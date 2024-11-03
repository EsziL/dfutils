package com.eszil.features.automation;

import com.eszil.config.Configuration;
import com.eszil.utils.RegexUtils;
import com.eszil.utils.ServerUtils;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class AutoChatLocal {

    public static void run(Text message) {
        if (!ServerUtils.isOnDF()) return;
        Configuration config = AutoConfig.getConfigHolder(Configuration.class).getConfig();
        if (!config.autoChatMode.enabled) return;

        MinecraftClient client = MinecraftClient.getInstance();

        if (RegexUtils.didJoinGame(message) && client.player != null) {
            client.player.networkHandler.sendChatCommand("c "+config.autoChatMode.mode.toString().toLowerCase());
        }



    }


}
