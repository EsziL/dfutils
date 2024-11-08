package com.eszil.utils;

import net.minecraft.text.Text;

public class TextUtils {

    public static Text addTextToText(Text t1, Text t2) {
        return t1.copy().append(t2);
    }

}
