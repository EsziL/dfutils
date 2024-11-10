package com.eszil.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class OtherUtils {

    public static boolean isKeyPressed(int key) {
        return GLFW.glfwGetKey(MinecraftClient.getInstance().getWindow().getHandle(), key) == GLFW.GLFW_PRESS;
    }

    public static ItemStack getItemStackWithNBT(ItemStack item, String nbt) {
        MinecraftClient client = MinecraftClient.getInstance();
        try {
            item.setNbt(StringNbtReader.parse(nbt));
            return item;
        } catch (Exception e)  {
            if (client.player != null) {
                client.player.sendMessage(Text.of(item.toString()));
                client.player.sendMessage(Text.of(e.toString()));
//                client.player.sendMessage(Text.of(item.toString()));
            }
            return ItemStack.EMPTY;
        }
    }

}
