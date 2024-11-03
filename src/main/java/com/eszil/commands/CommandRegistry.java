package com.eszil.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public class CommandRegistry {

    public static void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dfutils.register(dispatcher);
        ip.register(dispatcher);
    }
}
