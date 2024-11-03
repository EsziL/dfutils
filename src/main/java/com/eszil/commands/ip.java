package com.eszil.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class ip {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("ip")
                .executes(commandContext -> {
                    MinecraftClient client = MinecraftClient.getInstance();
                    if (client.player == null) {
                        client.inGameHud.getChatHud().addMessage(Text.literal("Player is not available."));
                        return Command.SINGLE_SUCCESS;
                    }

                    if (client.getCurrentServerEntry() != null) {
//                        String serverIp = client.player.getServer().getServerIp();
                        String serverIp = client.getCurrentServerEntry().address;
                        client.inGameHud.getChatHud().addMessage(Text.literal(serverIp));
                    } else {
                        client.inGameHud.getChatHud().addMessage(Text.literal("You aren't on a server."));
                    }
                    return Command.SINGLE_SUCCESS;
                })
        );
    }
}
