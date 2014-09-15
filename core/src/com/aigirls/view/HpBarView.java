package com.aigirls.view;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class HpBarView extends GameView{
    private Vector2 currentPosition;
    private Vector2 targetPosition;

    public HpBarView(int x, int y, int w, int h)
    {
        super(x, y, w, h);
        currentPosition = new Vector2(x+width, y);
        targetPosition = new Vector2(x+width, y);
    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0.6f, 0.3f, 1);
        currentPosition.lerp(targetPosition, 0.3f);
        shapeRenderer.rect(leftX, lowerY, currentPosition.x-leftX, height);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0, 0, 0, 1);
        shapeRenderer.rect(leftX, lowerY, width, height);
        shapeRenderer.end();
    }

    public void setRestHp(double changeRate) {
        int changeValue = (int)Math.round(changeRate*width);
        targetPosition.set(targetPosition.x+changeValue, lowerY);
    }

}
