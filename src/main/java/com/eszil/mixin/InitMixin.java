package com.eszil.mixin;

import com.eszil.config.Configuration;
import com.eszil.enums.DFNodes;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class InitMixin {

    @Unique
    private static boolean first = false;

    @Inject(method = "init", at=@At("RETURN"))
    private void onInit(CallbackInfo ci) {
        Configuration config = AutoConfig.getConfigHolder(Configuration.class).getConfig();

        if (!config.autoJoinDF.enabled || first) return;

        String add = "";
        if (config.autoJoinDF.autoJoinNode != DFNodes.None) add = config.autoJoinDF.autoJoinNode.toString().toLowerCase()+".";

        ServerInfo info = new ServerInfo("DiamondFire", add+"mcdiamondfire.com", ServerInfo.ServerType.OTHER);
        ConnectScreen.connect((TitleScreen) (Object) this, MinecraftClient.getInstance(), ServerAddress.parse(info.address), info, true);
        first = true;
    }

}
