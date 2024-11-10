package com.eszil.events;

import com.eszil.features.keybinds.FlightSpeedKeybinds;
import net.minecraft.client.MinecraftClient;

public class Events {

    public static void register() {
        ChatEvent.register();
        JoinEvent.register();
        EndClientTickEvent.register();
        LeaveEvent.register();
    }

    public static void tick(MinecraftClient client) {
        FlightSpeedKeybinds.tick(client);
    }
}
