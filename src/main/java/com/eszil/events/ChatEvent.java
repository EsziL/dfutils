package com.eszil.events;

import com.eszil.PublicValues;
import com.eszil.enums.DFMode;
import com.eszil.features.automation.AutoChatLocal;
import com.eszil.features.automation.AutoLagslayer;
import com.eszil.utils.RegexUtils;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.MinecraftClient;

public class ChatEvent {
    public static void register() {
        ClientReceiveMessageEvents.GAME.register((message, overlay) -> {
            AutoChatLocal.run(message);
            AutoLagslayer.run(message);

            if (RegexUtils.didJoinPlay(message)) {
                PublicValues.playerMode = DFMode.PLAY;
            } else if (RegexUtils.didJoinDev(message)) {
                PublicValues.playerMode = DFMode.DEV;
            } else if (RegexUtils.didJoinBuild(message)) {
                PublicValues.playerMode = DFMode.BUILD;
            }
        });

    }
}
