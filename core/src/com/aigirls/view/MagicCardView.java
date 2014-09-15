package com.aigirls.view;

import java.awt.Point;

import com.aigirls.config.GameConfig;
import com.aigirls.model.ChoiceModel;
import com.aigirls.model.battle.ActiveMagicModel;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class MagicCardView extends GameView {
    private static final double CARD_SPACE_RATE = 0.8;
    private Point[] ballPlaces;
    private boolean canUse;
    private int ballSize;
    private String magicName;
    private BitmapFont font;

    public MagicCardView(int x, int y, int cardWidth, ActiveMagicModel magic)
    {
        super(x, y, cardWidth, (int)Math.round(cardWidth/CARD_SPACE_RATE));
        ballSize = cardWidth / GameConfig.BOARD_WIDTH;
        font = new BitmapFont();
        if (magic != null) {
            ballPlaces = magic.getConditionBallPlaces();
            canUse = magic.canOutbreak();
            magicName = magic.getName();
        } else {
            ballPlaces = new Point[0];
            canUse = true;
            magicName = "戻る";
        }
    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer)
    {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1, 1, 1, 1);
        shapeRenderer.rect(leftX, lowerY, width, height);
        //ボールの描画
        int radius = ballSize/2;
        shapeRenderer.setColor(0.1f, 0.75f, 0.4f, 1);
        for (int i=0; i<ballPlaces.length;i++) {
            shapeRenderer.circle(leftX+radius, lowerY+radius, radius);
        }
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0, 0, 0, 1);
        shapeRenderer.rect(leftX, lowerY, width, height);
        shapeRenderer.line(leftX, (int)(lowerY+CARD_SPACE_RATE*height), leftX+width, (int)(lowerY+CARD_SPACE_RATE*height));
        shapeRenderer.end();

        batch.begin();
        font.setColor(0, 0, 0, 1);
        double spaceRate = 0.1;
        font.draw(batch, magicName, (int)(leftX+spaceRate*width), (int)(lowerY+(CARD_SPACE_RATE+spaceRate)*height));
        batch.end();

        if (canUse) return;

        //カード塗りつぶし
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.8f);
        shapeRenderer.rect(leftX, lowerY, width, height);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public ChoiceModel getChoiceModel()
    {
        return new ChoiceModel(leftX, lowerY, width, height);
    }

}
