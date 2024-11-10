package com.eszil.events;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

public class LeaveEvent {

    public static void register() {
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client)->{
            if (JoinEvent.wasOnDF) JoinEvent.wasOnDF = false;
        });
    }

}
