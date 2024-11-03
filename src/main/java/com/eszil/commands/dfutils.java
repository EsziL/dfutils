package com.eszil.commands;

import com.eszil.config.Configuration;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;

public class dfutils {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("dfutils")
                .executes(context -> {
                    MinecraftClient client = MinecraftClient.getInstance();
                    client.setScreen(AutoConfig.getConfigScreen(Configuration.class, null).get());
                    return Command.SINGLE_SUCCESS;
                })
        );
    }
}
