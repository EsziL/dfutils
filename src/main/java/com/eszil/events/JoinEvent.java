package com.eszil.events;

import com.eszil.config.Configuration;
import com.eszil.utils.ServerUtils;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.impl.command.client.ClientCommandInternals;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class JoinEvent {

    public static boolean wasOnDF = false;

    public static void register() {


        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if(ServerUtils.isOnDF()) {
                if (wasOnDF) return;
                wasOnDF = true;

                Executors.newSingleThreadScheduledExecutor().schedule(()->{
                    if (ClientCommandManager.getActiveDispatcher() != null) {
                        Configuration config = AutoConfig.getConfigHolder(Configuration.class).getConfig();

                        if (config.autoJoinDF.autoJoinPlot && client.player != null) {
                            client.player.networkHandler.sendChatCommand("join "+config.autoJoinDF.autoJoinPlotID);
                        }
                    } else if (client.player != null) {
                        client.player.sendMessage(Text.of("Could not auto-connect you to a plot! (report this on the discord)").copy().formatted(Formatting.RED));
                    }
                }, 100, TimeUnit.MILLISECONDS);


            } else wasOnDF = false;
        });
    }
}
