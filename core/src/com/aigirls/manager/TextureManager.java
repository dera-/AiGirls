package com.aigirls.manager;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class TextureManager
{
    private static HashMap<String,Texture> textureMap = new HashMap<String,Texture>();

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
