package com.aigirls.view;

import com.aigirls.config.FileConfig;
import com.aigirls.config.GameConfig;
import com.aigirls.manager.BitmapFontManager;
import com.aigirls.manager.TextureManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class GameStartScreenView extends GameView {
    private static int TITLE_X = (int) Math.round(0.1*GameConfig.GAME_WIDTH);
    private static int TITLE_Y = (int) Math.round(0.8*GameConfig.GAME_HEIGHT);
    private static int IMAGE_X = (int) Math.round(0.5*GameConfig.GAME_WIDTH);
    private static int IMAGE_Y = (int) Math.round(0.05*GameConfig.GAME_HEIGHT);
    private static int IMAGE_WIDTH = (int) Math.round(0.2*GameConfig.GAME_WIDTH);
    private static int IMAGE_HEIGHT = (int) Math.round(0.6*GameConfig.GAME_WIDTH);
    private Sprite titleSprite;
    private BitmapFont font;
    private final int realWidth;
    private final int realHeight;


    static {
        TextureManager.generateTexture(FileConfig.TITLE_IMAGE_PATH, FileConfig.TITLE_IMAGE_KEY);
    }

    public GameStartScreenView()
    {
        font = BitmapFontManager.getBitmapFont(FileConfig.NYANKO_FONT_KEY);
        Texture texture = TextureManager.getTexture(FileConfig.TITLE_IMAGE_KEY);
        titleSprite = new Sprite(texture);
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
    public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer)
    {
        batch.begin();
        font.setColor(1, 1, 1, 1);
        font.draw(batch, GameConfig.TITLE, TITLE_X, TITLE_Y);
        titleSprite.setSize(realWidth, realHeight);
        titleSprite.setPosition(IMAGE_X, IMAGE_Y);
        titleSprite.draw(batch);
        batch.end();
    }

}
