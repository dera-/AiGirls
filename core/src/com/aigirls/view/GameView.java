package com.aigirls.view;

import com.aigirls.config.GameConfig;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class GameView {
    protected final int leftX;
    protected final int lowerY;
    protected final int width;
    protected final int height;

    public GameView() {
        this(0, 0, GameConfig.GAME_WIDTH, GameConfig.GAME_HEIGHT);
    }

    public GameView(int x, int y, int w, int h) {
        leftX = x;
        lowerY = y;
        width = w;
        height = h;
    }

    public abstract void draw(SpriteBatch batch, ShapeRenderer shapeRenderer);
}
