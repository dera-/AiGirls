package com.aigirls.manager;

import java.util.HashMap;

import com.aigirls.config.FileConfig;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class BitmapFontManager {
    private static HashMap<String,BitmapFont> fontMap = new HashMap<String,BitmapFont>();

    static {
        generateBitmapFont(FileConfig.GOTHIC_FONT_KEY);
        generateBitmapFont(FileConfig.NYANKO_FONT_KEY);
    }

    public static void setBitmapFont(String name, BitmapFont bitmapFont)
    {
        fontMap.put(name, bitmapFont);
    }

    public static BitmapFont getBitmapFont(String name)
    {
        return fontMap.get(name);
    }

    public static BitmapFont generateBitmapFont(String name)
    {
        if (fontMap.containsKey(name)) {
            return fontMap.get(name);
        }
        BitmapFont bitmapFont = new BitmapFont(
            FileManager.getFileHandle(FileConfig.FONT_DIR_PATH + name + ".fnt"),
            FileManager.getFileHandle(FileConfig.FONT_DIR_PATH + name + "_0.png"),
            false);
        setBitmapFont(name, bitmapFont);
        return bitmapFont;
    }
}
