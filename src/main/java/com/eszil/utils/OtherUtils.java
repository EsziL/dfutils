package com.eszil.utils;

import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

public class OtherUtils {

    public static boolean isKeyPressed(int key) {
        return GLFW.glfwGetKey(MinecraftClient.getInstance().getWindow().getHandle(), key) == GLFW.GLFW_PRESS;
    }

}
