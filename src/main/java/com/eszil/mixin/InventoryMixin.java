package com.eszil.mixin;

import com.eszil.PublicValues;
import com.eszil.enums.DFMode;
import com.eszil.utils.OtherUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@Mixin(CreativeInventoryScreen.class)
public class InventoryMixin {

    @Unique
    private static final Identifier SLOT_TEXTURE = new Identifier("minecraft", "textures/gui/container/generic_54.png");
    @Unique
    private final List<ItemStack> items = new ArrayList<>(Arrays.asList(
            new ItemStack(Items.DIAMOND),
            new ItemStack(Items.GOLD_BLOCK)
    ));
    @Unique
    private final int split = 5;
    @Unique
    private boolean cancelDrop = false;
    @Unique

    @Inject(method = "drawBackground", at = @At("TAIL"))
    private void drawCustomInventory(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (PublicValues.playerMode != DFMode.DEV) return;

        int x = client.getWindow().getScaledWidth() / 2 - 190;
        int y = client.getWindow().getScaledHeight() / 2 - 80;
        int size = items.size();

        for (int i = 0; i < (int) Math.ceil((double) size / split)*split; i++) {
            int slotX = x + (i % split) * 18;
            int slotY = y + (i / split) * 18;

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

        if (PublicValues.playerMode != DFMode.DEV || client.player == null) return;

        int x = client.getWindow().getScaledWidth() / 2 - 190;
        int y = client.getWindow().getScaledHeight() / 2 - 80;

        for (int i = 0; i < items.size(); i++) {
            int slotX = x + (i % split) * 18;
            int slotY = y + (i / split) * 18;

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

