package com.aigirls.manager;

import java.util.HashMap;

import com.aigirls.config.FileConfig;
import com.badlogic.gdx.graphics.Texture;

public class TextureManager
{
    private static HashMap<String,Texture> textureMap = new HashMap<String,Texture>();

    static {
        generateTexture(FileConfig.BUTTOM_IMAGE_PATH, FileConfig.BUTTOM_KEY);
        generateTexture(FileConfig.RIGHT_ARROW_IMAGE_PATH, FileConfig.RIGHT_ARROW_KEY);
        generateTexture(FileConfig.LEFT_ARROW_IMAGE_PATH, FileConfig.LEFT_ARROW_KEY);
        generateTexture(FileConfig.STAR_IMAGE_PATH, FileConfig.STAR_KEY);
    }

    public static void setTexture(String name, Texture texture)
    {
        textureMap.put(name, texture);
    }

    public static Texture getTexture(String name)
    {
        return textureMap.get(name);
    }

    public static Texture generateTexture(String path, String name)
    {
        if (textureMap.containsKey(name)) {
            return textureMap.get(name);
        }
        Texture texture = new Texture(FileManager.getFileHandle(path));
        setTexture(name, texture);
        return texture;
    }
}
