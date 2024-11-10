package com.eszil.features.keybinds;

import com.eszil.config.Configuration;
import com.eszil.utils.ServerUtils;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;


public class FlightSpeedKeybinds {

    private static KeyBinding keybindMedium = null;
    private static KeyBinding keybindHigh = null;

    public static void register() {
        keybindMedium = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.dfutils.flightspeedmedium",
            InputUtil.Type.KEYSYM,
            -1,
            "key.category.dfutils"
        ));

        keybindHigh = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.dfutils.flightspeedhigh",
            InputUtil.Type.KEYSYM,
            -1,
            "key.category.dfutils"
        ));



    }

    public static void tick(MinecraftClient client) {
        if (client.player != null && ServerUtils.isOnDF() && keybindMedium != null && keybindHigh != null) {
            Configuration config = AutoConfig.getConfigHolder(Configuration.class).getConfig();
            int upwardSpeed = Math.round(client.player.getAbilities().getFlySpeed()*2000);

            String command = "fs ";
            if (keybindMedium.wasPressed()) {
                if (upwardSpeed != config.flightSpeedValues.medium) command += config.flightSpeedValues.medium;
                else command += config.flightSpeedValues.normal;
            } else if (keybindHigh.wasPressed()) {
                if (upwardSpeed != config.flightSpeedValues.high) command += config.flightSpeedValues.high;
                else command += config.flightSpeedValues.normal;
            }

            if (!command.equals("fs ")) client.player.networkHandler.sendChatCommand(command);
        }
    }
}
