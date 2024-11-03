package com.eszil.features.automation;

import com.eszil.config.Configuration;
import com.eszil.utils.RegexUtils;
import com.eszil.utils.ServerUtils;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class AutoLagslayer {

    public static void run(Text message) {
        if (!ServerUtils.isOnDF()) return;
        Configuration config = AutoConfig.getConfigHolder(Configuration.class).getConfig();

        if (config.autoLagslayer) {
            MinecraftClient client = MinecraftClient.getInstance();

            if (RegexUtils.didJoinDev(message) && client.player != null) {
                client.player.networkHandler.sendChatCommand("lagslayer");
            }
        }


    }


}
