package com.aigirls.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.aigirls.config.FileConfig;
import com.aigirls.entity.BaseEntity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureManager
{
    private static HashMap<String,Texture> textureMap = new HashMap<String,Texture>();
    private static HashMap<String,TextureRegion[]> textureRegionMap = new HashMap<String,TextureRegion[]>();

    static {
        generateTexture(FileConfig.BUTTOM_IMAGE_PATH, FileConfig.BUTTOM_KEY);
        generateTexture(FileConfig.RIGHT_ARROW_IMAGE_PATH, FileConfig.RIGHT_ARROW_KEY);
        generateTexture(FileConfig.LEFT_ARROW_IMAGE_PATH, FileConfig.LEFT_ARROW_KEY);
        generateTexture(FileConfig.STAR_IMAGE_PATH, FileConfig.STAR_KEY);
        generateTexture(FileConfig.SMALL_DAMAGE_IMAGE_PATH, FileConfig.SMALL_DAMAGE_KEY);
        generateTexture(FileConfig.BIG_DAMAGE_IMAGE_PATH, FileConfig.BIG_DAMAGE_KEY);

        //以下、アニメーション用の画像読み込み
        generateTextureRegions(FileConfig.BLUE_STAR_IMAGE_PATH, FileConfig.BLUE_STAR_KEY, FileConfig.IMAGE_SPLITTED_WIDTH, FileConfig.IMAGE_SPLITTED_HEIGHT);
        generateTextureRegions(FileConfig.RED_STAR_IMAGE_PATH, FileConfig.RED_STAR_KEY, FileConfig.IMAGE_SPLITTED_WIDTH, FileConfig.IMAGE_SPLITTED_HEIGHT);
        generateTextureRegions(FileConfig.BLUE_RECOVER_IMAGE_PATH, FileConfig.BLUE_RECOVER_KEY, FileConfig.IMAGE_SPLITTED_WIDTH, FileConfig.IMAGE_SPLITTED_HEIGHT);
        generateTextureRegions(FileConfig.RED_RECOVER_IMAGE_PATH, FileConfig.RED_RECOVER_KEY, FileConfig.IMAGE_SPLITTED_WIDTH, FileConfig.IMAGE_SPLITTED_HEIGHT);
        generateTextureRegions(FileConfig.ATTACK_IMAGE_PATH, FileConfig.ATTACK_KEY, FileConfig.ATTACK_IMAGE_SPLITTED_WIDTH, FileConfig.IMAGE_SPLITTED_HEIGHT);
        generateTextureRegions(FileConfig.EXPLODE_IMAGE_PATH, FileConfig.EXPLODE_KEY, FileConfig.IMAGE_SPLITTED_WIDTH, FileConfig.IMAGE_SPLITTED_HEIGHT);

        //以下、クラウディアさん
        String keyPrefix = "cloud_";
        String pathPrefix = FileConfig.IMAGE_DIR_PATH + keyPrefix;
        TextureManager.generateTexture(pathPrefix+FileConfig.CHARA_EXPRESSION_KEY_NORMAL+".jpg", keyPrefix+FileConfig.CHARA_EXPRESSION_KEY_NORMAL);
        TextureManager.generateTexture(pathPrefix+FileConfig.CHARA_EXPRESSION_KEY_WIN+".jpg", keyPrefix+FileConfig.CHARA_EXPRESSION_KEY_WIN);
        TextureManager.generateTexture(pathPrefix+FileConfig.CHARA_EXPRESSION_KEY_LOSE+".jpg", keyPrefix+FileConfig.CHARA_EXPRESSION_KEY_LOSE);
        TextureManager.generateTexture(pathPrefix+FileConfig.CHARA_EXPRESSION_KEY_WAIT+".jpg", keyPrefix+FileConfig.CHARA_EXPRESSION_KEY_WAIT);
        TextureManager.generateTexture(pathPrefix+FileConfig.CHARA_EXPRESSION_KEY_ATTACK+".jpg", keyPrefix+FileConfig.CHARA_EXPRESSION_KEY_ATTACK);
        TextureManager.generateTexture(pathPrefix+FileConfig.CHARA_EXPRESSION_KEY_PINCH+".jpg", keyPrefix+FileConfig.CHARA_EXPRESSION_KEY_PINCH);
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

    public static void setTextureRegions(String name, TextureRegion[] regions)
    {
        textureRegionMap.put(name, regions);
    }

    public static TextureRegion[] getTextureRegion(String name)
    {
        return textureRegionMap.get(name);
    }

    public static TextureRegion[] generateTextureRegions(String path, String name, int w, int h)
    {
        if (textureRegionMap.containsKey(name)) {
            return getTextureRegion(name);
        }
        Texture texture = new Texture(FileManager.getFileHandle(path));
        List<TextureRegion> list = new ArrayList<TextureRegion>();
        TextureRegion[][] regions = new TextureRegion(texture).split(w, h);
        for (int i=0; i<regions.length; i++) {
            for (int j=0; j<regions[i].length; j++) {
                list.add(regions[i][j]);
            }
        }
        TextureRegion[] region = (TextureRegion[])list.toArray(new TextureRegion[list.size()]);
        setTextureRegions(name, region);
        return region;
    }

    public static TextureRegion[] getTextureRegions(String[] paths, String[] keys, int num)
    {
        TextureRegion[] regions = new TextureRegion[num];
        for (int i=0; i<num; i++) {
            regions[i] = new TextureRegion(generateTexture(paths[i], keys[i]));
        }
        return regions;
    }

}
