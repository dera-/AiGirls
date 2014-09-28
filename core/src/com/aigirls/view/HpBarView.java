package com.aigirls.view;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class HpBarView extends GameView{
    private Vector2 currentPosition;
    private Vector2 targetPosition;
    private int maxHp;

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
        shapeRenderer.setColor(1, 1, 1, 1);
        shapeRenderer.rect(leftX, lowerY, width, height);
        shapeRenderer.end();
    }

    public void setRestHp(double changeRate) {
        int changeValue = (int)Math.round(changeRate*width);
        int currentBarXPlace = (int)(targetPosition.x-changeValue);
        if (currentBarXPlace < leftX) {
            currentBarXPlace = leftX;
        }
        targetPosition.set(currentBarXPlace, lowerY);
    }

    //TODO こっちに移行予定
    public void setRestHp(int damage) {
        int changeValue = (int) Math.round(1.0*damage*width/maxHp);
        int currentBarXPlace = (int)(targetPosition.x-changeValue);
        if (currentBarXPlace < leftX) {
            currentBarXPlace = leftX;
        }
        targetPosition.set(currentBarXPlace, lowerY);
    }

}
