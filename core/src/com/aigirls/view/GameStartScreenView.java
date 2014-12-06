package com.aigirls.view;

import com.aigirls.config.FileConfig;
import com.aigirls.config.GameConfig;
import com.aigirls.manager.BitmapFontManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class GameStartScreenView extends GameView {
    private static int TITLE_X = (int) Math.round(0.1*GameConfig.GAME_WIDTH);
    private static int TITLE_Y = (int) Math.round(0.1*GameConfig.GAME_HEIGHT);
    private Sprite titleSprite;
    private BitmapFont font;

    static {

    }

    public GameStartScreenView()
    {
        font = BitmapFontManager.getBitmapFont(FileConfig.NYANKO_FONT_KEY);
    }


    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer)
    {
        batch.begin();
        font.setColor(1, 1, 1, 1);
        font.draw(batch, GameConfig.TITLE, TITLE_X, TITLE_Y);
        batch.end();
    }

}
