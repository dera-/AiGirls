package com.aigirls.view;

import com.aigirls.config.FileConfig;
import com.aigirls.manager.BitmapFontManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class HpBarView extends GameView{
    private Vector2 currentPosition;
    private Vector2 targetPosition;
    private BitmapFont font;
    private final int maxHp;

    public HpBarView(int x, int y, int w, int h, int hp)
    {
        super(x, y, w, h);
        currentPosition = new Vector2(x+width, y);
        targetPosition = new Vector2(x+width, y);
        font = BitmapFontManager.getBitmapFont(FileConfig.NYANKO_FONT_KEY);
        maxHp = hp;
    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0.6f, 0.3f, 1);
        currentPosition.lerp(targetPosition, 0.3f);
        shapeRenderer.rect(leftX, lowerY, currentPosition.x-leftX, height);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 1, 1, 1);
        shapeRenderer.rect(leftX, lowerY, width, height);
        shapeRenderer.end();

        batch.begin();
        font.setColor(1, 1, 1, 1);
        double spaceRateX = 0.1;
        double spaceRateY = 0.18;
        //HPの値の描画
        int currentHp = (int) Math.round(1.0 * maxHp * (targetPosition.x - leftX) / width);
        font.draw(batch, currentHp + "/" + maxHp, (int)(leftX+spaceRateX*width), (int)(lowerY + (1-spaceRateY) * height));
        batch.end();
    }

    public void setRestHp(int damage) {
        int changeValue = (int) Math.round(1.0*damage*width/maxHp);
        int currentBarXPlace = (int)(targetPosition.x-changeValue);
        if (currentBarXPlace < leftX) {
            currentBarXPlace = leftX;
        }
        targetPosition.set(currentBarXPlace, lowerY);
    }

}
