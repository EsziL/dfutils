package com.eszil;

import com.eszil.config.Configuration;
import com.eszil.events.Events;
import com.eszil.features.keybinds.Keybinds;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

import static com.eszil.commands.CommandRegistry.registerCommands;

public class Dfutils implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		Events.register();
		Keybinds.register();

		AutoConfig.register(Configuration.class, Toml4jConfigSerializer::new);
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> registerCommands(dispatcher));
	}
}
