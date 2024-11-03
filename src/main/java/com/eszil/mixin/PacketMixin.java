package com.eszil.mixin;

import com.eszil.features.screen.RelocateLagslayer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.OverlayMessageS2CPacket;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class PacketMixin {

    @Inject(method = "onOverlayMessage", at = @At("HEAD"), cancellable = true)
    public void onOverlayMessage(OverlayMessageS2CPacket packet, CallbackInfo ci) {
        if (RelocateLagslayer.run(packet.getMessage())) ci.cancel();
    }
}
