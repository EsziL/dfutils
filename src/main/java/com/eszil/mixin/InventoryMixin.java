package com.eszil.mixin;

import com.eszil.PublicValues;
import com.eszil.config.Configuration;
import com.eszil.enums.DFMode;
import com.eszil.utils.OtherUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.*;

@Mixin(CreativeInventoryScreen.class)
public class InventoryMixin {

    @Unique
    private static final Identifier SLOT_TEXTURE = new Identifier("minecraft", "textures/gui/container/generic_54.png");
    @Unique
    private static final List<ItemStack> items = new ArrayList<>();
    @Unique
    private final int split = 6;
    @Unique
    private boolean cancelDrop = false;

    static {
        Gson gson = new Gson();

        MinecraftClient client = MinecraftClient.getInstance();
        ResourceManager resourceManager = client.getResourceManager();

        List<String> types = new ArrayList<>();
        List<String> blocks = new ArrayList<>();

        Identifier codeBlocksIdentifier = new Identifier("dfutils", "data/codeblocks.json");

        try {
            Resource resource = resourceManager.getResource(codeBlocksIdentifier).orElseThrow();

            try (InputStreamReader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
                JsonElement rootElement = gson.fromJson(reader, JsonElement.class);

                if (rootElement.isJsonArray()) {
                    JsonArray rootArray = rootElement.getAsJsonArray();
                    for (JsonElement entryEl : rootArray) {
                        JsonObject entry = entryEl.getAsJsonObject();

                        JsonElement typeElement = entry.get("type");
                        JsonElement blockElement = entry.get("block");
                        if (typeElement.isJsonPrimitive()) types.add(typeElement.getAsString());
                        if (blockElement.isJsonPrimitive()) blocks.add(blockElement.getAsString());
                    }
                }
            }

            for (int i = 0; i < types.size(); i++) {
                items.add(OtherUtils.getItemStackWithNBT(new ItemStack(Registries.ITEM.get(new Identifier(types.get(i)))), blocks.get(i)));
            }

        } catch (Exception e) {
            if (client.player != null) {
                client.player.sendMessage(Text.of("Error: " + e.toString()));
            }
        }
    }

    @Inject(method = "drawBackground", at = @At("TAIL"))
    private void drawCustomInventory(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo ci) {
        Configuration config = AutoConfig.getConfigHolder(Configuration.class).getConfig();
        if (!config.codeGUI) return;
        MinecraftClient client = MinecraftClient.getInstance();

        if (PublicValues.playerMode != DFMode.DEV) return;

        int x = client.getWindow().getScaledWidth() / 2 - 210;
        int y = client.getWindow().getScaledHeight() / 2 - 68;
        int size = items.size();

        int rowOffset = 0;
        for (int i = 0; i < (int) Math.ceil((double) size / split)*split; i++) {
            if (i < size && items.get(i).getItem() == Items.SPECTRAL_ARROW) {
                rowOffset++;

                for (int j = 0; j < split; j++) {
                    int emptySlotX = x + j * 18;
                    int emptySlotY = y + (i / split) * 18;
                    context.drawTexture(SLOT_TEXTURE, emptySlotX, emptySlotY, 7, 89, 18, 18);
                    if (isMouseOverSlot(mouseX, mouseY, emptySlotX, emptySlotY)) {
                        drawSlotHighlight(context, emptySlotX, emptySlotY);
                    }
                }
            }
            int slotX = x + (i % split) * 18;
            int slotY = y + (i / split + rowOffset) * 18;

            context.drawTexture(SLOT_TEXTURE, slotX, slotY, 7, 89, 18, 18);

            if (i < size) {
                ItemStack stack = items.get(i);
                context.drawItem(stack, slotX + 1, slotY + 1);
                context.drawItemInSlot(client.textRenderer, stack, slotX + 1, slotY + 1);
                if (isMouseOverSlot(mouseX, mouseY, slotX, slotY)) {
                    context.drawItemTooltip(client.textRenderer, stack, mouseX, mouseY);
                }
            }

            if (isMouseOverSlot(mouseX, mouseY, slotX, slotY)) {
                drawSlotHighlight(context, slotX, slotY);
            }
        }
    }

    @Inject(method = "mouseClicked", at= @At("HEAD"), cancellable = true)
    private void onMouseClick(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        MinecraftClient client = MinecraftClient.getInstance();
        Configuration config = AutoConfig.getConfigHolder(Configuration.class).getConfig();
        if (!config.codeGUI) return;

        if (PublicValues.playerMode != DFMode.DEV || client.player == null) return;

        int x = client.getWindow().getScaledWidth() / 2 - 210;
        int y = client.getWindow().getScaledHeight() / 2 - 68;

        for (int i = 0; i < items.size(); i++) {
            int slotX = x + (i % split) * 18;
            int slotY = y + (i / split + i / 18) * 18;

            if(isMouseOverSlot((int) mouseX, (int) mouseY, slotX, slotY) && button == 0) {
                ItemStack stack = items.get(i);

                if (OtherUtils.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT)) {
                    client.player.giveItemStack(stack.copy());
                } else {
                    // evil string conversion check because im stupid and cant figure out another way of doing it
                    if (client.player.currentScreenHandler.getCursorStack().toString().equals("0 air")) client.player.currentScreenHandler.setCursorStack(stack.copy());
                    cancelDrop = true;
                }
                cir.setReturnValue(true);
                return;
            }
        }

    }

    @Inject(method = "mouseReleased", at = @At("HEAD"), cancellable = true)
    private void onMouseRelease(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (cancelDrop) {
            cir.setReturnValue(true);
            cancelDrop = false;
        }
    }

    @Unique
    private boolean isMouseOverSlot(int mouseX, int mouseY, int slotX, int slotY) {
        return mouseX >= slotX && mouseX < slotX + 18 && mouseY >= slotY && mouseY < slotY + 18;
    }

    @Unique
    private void drawSlotHighlight(DrawContext context, int slotX, int slotY) {
        int color = 0x80FFFFFF;
        context.fill(slotX+1, slotY+1, slotX + 18, slotY + 18, color);
    }
}

