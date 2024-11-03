package com.eszil.events;

import com.eszil.PublicValues;
import com.eszil.enums.DFMode;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.world.GameMode;

public class EndClientTickEvent {

    public static GameMode lastGameMode = null;

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            Events.tick(client);


            if (client.player != null && client.interactionManager != null) {
                GameMode currentGameMode = client.interactionManager.getCurrentGameMode();
                if (lastGameMode != currentGameMode) {
                    // Gamemode Change Event

                    if (currentGameMode == GameMode.ADVENTURE && (PublicValues.playerMode == DFMode.BUILD || PublicValues.playerMode == DFMode.DEV)) {
                        PublicValues.playerMode = DFMode.PLAY;
                    }

                }
            }
        });
    }
}
