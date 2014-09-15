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
    private static final int EXPRESSION_NUMS = 3;

    private Sprite[] charaSprites;
    private int currentIndex;
    private CharacterViewModel characterViewModel;

    public CharacterView(int x, int y, int w, int h, CharacterViewModel viewModel)
    {
        super(x, y, w, h);
        currentIndex = EXPRESSION_NORMAL;
        characterViewModel = viewModel;
        String keyPrefix = characterViewModel.getImageName() + "_";
        String pathPrefix = FileConfig.FILE_DIR_PATH + keyPrefix;
        charaSprites = new Sprite[EXPRESSION_NUMS];
        charaSprites[EXPRESSION_NORMAL] = new Sprite(
            TextureManager.generateTexture(pathPrefix+FileConfig.CHARA_EXPRESSION_KEY_NORMAL+FileConfig.IMAGE_FORMAT, keyPrefix+FileConfig.CHARA_EXPRESSION_KEY_NORMAL), x, y, w, h);
        charaSprites[EXPRESSION_WIN] = new Sprite(
            TextureManager.generateTexture(pathPrefix+FileConfig.CHARA_EXPRESSION_KEY_WIN+FileConfig.IMAGE_FORMAT, keyPrefix+FileConfig.CHARA_EXPRESSION_KEY_WIN), x, y, w, h);
        charaSprites[EXPRESSION_LOSE] = new Sprite(
            TextureManager.generateTexture(pathPrefix+FileConfig.CHARA_EXPRESSION_KEY_LOSE+FileConfig.IMAGE_FORMAT, keyPrefix+FileConfig.CHARA_EXPRESSION_KEY_LOSE), x, y, w, h);
    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer)
    {
        batch.begin();
        charaSprites[currentIndex].draw(batch);
        batch.end();
    }

    public void changeExpression(int index)
    {
        if(index >= EXPRESSION_NUMS) return;
        currentIndex = index;
    }

}
