package com.aigirls.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class FilledView extends GameView {
    private Color colorToFill;
    private Vector2 place;

    public FilledView (int x, int y, int w, int h, Color color) {
        super(x, y, w, h);
        colorToFill = color;
        place = new Vector2(x, y);
    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(colorToFill);
        shapeRenderer.rect(place.x, place.y, width, height);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public void setPlace(int x, int y)
    {
        place.set(x, y);
    }

    public void setDefaultPlace()
    {
        place.set(leftX, lowerY);
    }

}
