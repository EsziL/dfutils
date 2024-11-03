package com.eszil.events;

import com.eszil.utils.ServerUtils;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.text.Text;

public class JoinEvent {

    public static void register() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {

        });
    }
}
