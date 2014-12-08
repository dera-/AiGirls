package com.aigirls.view;

import com.aigirls.config.FileConfig;
import com.aigirls.manager.TextureManager;
import com.aigirls.model.battle.CharacterViewModel;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class CharacterView extends GameView
{
    public static final int EXPRESSION_NORMAL = 0;
    public static final int EXPRESSION_WIN  = 1;
    public static final int EXPRESSION_LOSE = 2;
    public static final int EXPRESSION_WAIT = 3;
    public static final int EXPRESSION_ATTACK = 4;
    public static final int EXPRESSION_DAMEGE = 5;
    private static final int EXPRESSION_NUMS = 6;

    private final int realWidth;
    private final int realHeight;
    private Sprite[] charaSprites;
    private int currentIndex;
    private CharacterViewModel characterViewModel;

    public CharacterView(int x, int y, int w, int h, CharacterViewModel viewModel)
    {
        super(x, y, w, h);
        currentIndex = EXPRESSION_NORMAL;
        characterViewModel = viewModel;
        String keyPrefix = characterViewModel.getImageName() + "_";
        String pathPrefix = FileConfig.IMAGE_DIR_PATH + keyPrefix;
        charaSprites = new Sprite[EXPRESSION_NUMS];
        charaSprites[EXPRESSION_NORMAL] = new Sprite(
            TextureManager.generateTexture(pathPrefix+FileConfig.CHARA_EXPRESSION_KEY_NORMAL+FileConfig.IMAGE_FORMAT_PNG, keyPrefix+FileConfig.CHARA_EXPRESSION_KEY_NORMAL));
        charaSprites[EXPRESSION_WIN] = new Sprite(
            TextureManager.generateTexture(pathPrefix+FileConfig.CHARA_EXPRESSION_KEY_WIN+FileConfig.IMAGE_FORMAT_PNG, keyPrefix+FileConfig.CHARA_EXPRESSION_KEY_WIN));
        charaSprites[EXPRESSION_LOSE] = new Sprite(
            TextureManager.generateTexture(pathPrefix+FileConfig.CHARA_EXPRESSION_KEY_LOSE+FileConfig.IMAGE_FORMAT_PNG, keyPrefix+FileConfig.CHARA_EXPRESSION_KEY_LOSE));
        charaSprites[EXPRESSION_WAIT] = new Sprite(
            TextureManager.generateTexture(pathPrefix+FileConfig.CHARA_EXPRESSION_KEY_WAIT+FileConfig.IMAGE_FORMAT_PNG, keyPrefix+FileConfig.CHARA_EXPRESSION_KEY_WAIT));
        charaSprites[EXPRESSION_ATTACK] = new Sprite(
            TextureManager.generateTexture(pathPrefix+FileConfig.CHARA_EXPRESSION_KEY_ATTACK+FileConfig.IMAGE_FORMAT_PNG, keyPrefix+FileConfig.CHARA_EXPRESSION_KEY_ATTACK));
        charaSprites[EXPRESSION_DAMEGE] = new Sprite(
            TextureManager.generateTexture(pathPrefix+FileConfig.CHARA_EXPRESSION_KEY_PINCH+FileConfig.IMAGE_FORMAT_PNG, keyPrefix+FileConfig.CHARA_EXPRESSION_KEY_PINCH));
        float imageWidth = charaSprites[EXPRESSION_NORMAL].getWidth();
        float imageHeight = charaSprites[EXPRESSION_NORMAL].getHeight();
        if (imageWidth < imageHeight) {
            realWidth = (int) Math.round(height * imageWidth / imageHeight);
            realHeight = height;
        } else {
            realWidth = width;
            realHeight = (int) Math.round(width * imageHeight / imageWidth);
        }

    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer)
    {
        batch.begin();
        charaSprites[currentIndex].setSize(realWidth, realHeight);
        charaSprites[currentIndex].setPosition(leftX, lowerY);

        charaSprites[currentIndex].draw(batch);
        batch.end();
    }

    public void changeExpression(int index)
    {
        if(index >= EXPRESSION_NUMS) return;
        currentIndex = index;
    }

}
