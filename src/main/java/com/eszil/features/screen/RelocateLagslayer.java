package com.eszil.features.screen;

import com.eszil.config.Configuration;
import com.eszil.utils.OtherUtils;
import com.eszil.utils.ServerUtils;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.List;
import java.util.regex.Pattern;

public class RelocateLagslayer {

    private static boolean toRender = false;
    private static Text relocatedMessage = null;
    private static long lastMessageTime = 0;

    private static final MinecraftClient client = MinecraftClient.getInstance();

    public static boolean run(Text message) {
        if (!ServerUtils.isOnDF()) return false;
        Configuration config = AutoConfig.getConfigHolder(Configuration.class).getConfig();
        if (!config.relocateLagslayer) return false;


        String messageAsString = message.getString();
        if (Pattern.compile("^CPU Usage: \\[▮{20}] \\(\\d{1,3}\\.\\d+%\\)$").matcher(messageAsString).find()) {

            relocatedMessage = message;
            toRender = true;
            lastMessageTime = System.currentTimeMillis();

            return true;
        }
        return false;
    }

    static {
        if (ServerUtils.isOnDF()) {
            HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastMessageTime > 3000) {
                    toRender = false;
                }

                if (toRender && relocatedMessage != null) {
                    List<Text> siblings = relocatedMessage.getSiblings();

                    matrixStack.drawTextWithShadow(client.textRenderer, siblings.getFirst(), 5, client.getWindow().getScaledHeight() - (client.textRenderer.fontHeight*3) - 5, 0xFFFFFF);
                    matrixStack.drawTextWithShadow(client.textRenderer, siblings.get(1).copy().append(siblings.get(2).copy().append(siblings.get(3))), 5, client.getWindow().getScaledHeight() - (client.textRenderer.fontHeight*2) - 5, 0xFFFFFF);
                    matrixStack.drawTextWithShadow(client.textRenderer, siblings.get(4).copy().append(siblings.get(5)), 5, client.getWindow().getScaledHeight() - client.textRenderer.fontHeight - 5, 0xFFFFFF);
                }
            });
        }
    }

}