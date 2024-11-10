package com.eszil.utils;

import net.minecraft.text.Text;

import java.util.List;

public class TextUtils {

    public static Text addTextToText(Text t1, Text t2) {
        return t1.copy().append(t2);
    }


    public static Text joinTexts(List<Text> t) {
        Text r = t.getFirst();
        for (int i = 1; i < t.toArray().length; i++) r = addTextToText(r, t.get(i));
        return r;
    }

}
