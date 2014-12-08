package com.aigirls.view.battle;

import com.aigirls.config.FileConfig;
import com.aigirls.config.GameConfig;
import com.aigirls.manager.BitmapFontManager;
import com.aigirls.manager.TextureManager;
import com.aigirls.view.GameView;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class LoadingBattleScreenView extends GameView {
    private static final int IMAGE_WIDTH = (int) Math.round(0.3*GameConfig.GAME_WIDTH);
    private static final int IMAGE_HEIGHT = (int) Math.round(0.3*GameConfig.GAME_HEIGHT);
    private static final int IMAGE_X = (int) Math.round(0.35*GameConfig.GAME_WIDTH);
    private static final int IMAGE_Y = (int) Math.round(0.35*GameConfig.GAME_HEIGHT);
    private static final int LOADING_STRING_X = (int) Math.round(0.6*GameConfig.GAME_WIDTH);
    private static final int LOADING_STRING_Y = (int) Math.round(0.1*GameConfig.GAME_HEIGHT);

    private Sprite sleepingSprite;
    private BitmapFont font;

    private int realWidth;
    private int realHeight;

    static {
        TextureManager.generateTexture(FileConfig.SLEEPING_IMAGE_PATH, FileConfig.SLEEPING_IMAGE_KEY);
    }

    public LoadingBattleScreenView()
    {
        font = BitmapFontManager.getBitmapFont(FileConfig.NYANKO_FONT_KEY);
        Texture texture = TextureManager.getTexture(FileConfig.SLEEPING_IMAGE_KEY);
        sleepingSprite = new Sprite(texture);
        int width = texture.getWidth();
        int height = texture.getHeight();
        if (width < height) {
            realWidth = (int) Math.round(IMAGE_HEIGHT * width / height);
            realHeight = IMAGE_HEIGHT;
        } else {
            realWidth = IMAGE_WIDTH;
            realHeight = (int) Math.round(IMAGE_WIDTH * height / width);
        }
    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        batch.begin();
        font.setColor(1, 1, 1, 1);
        font.draw(batch, "なうろーでぃんぐ", LOADING_STRING_X, LOADING_STRING_Y);
        sleepingSprite.setSize(realWidth, realHeight);
        sleepingSprite.setPosition(IMAGE_X, IMAGE_Y);
        sleepingSprite.draw(batch);
        batch.end();
    }
}
