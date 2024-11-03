package com.eszil.events;

import com.eszil.features.automation.AutoChatLocal;
import com.eszil.features.automation.AutoLagslayer;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.MinecraftClient;

public class ChatEvent {
    public static void register() {
        ClientReceiveMessageEvents.GAME.register((message, overlay) -> {
            AutoChatLocal.run(message);
            AutoLagslayer.run(message);
        });


    }
}
