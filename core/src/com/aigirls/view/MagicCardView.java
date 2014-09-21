package com.aigirls.view;

import java.awt.Point;

import com.aigirls.config.FileConfig;
import com.aigirls.config.GameConfig;
import com.aigirls.manager.BitmapFontManager;
import com.aigirls.model.ChoiceModel;
import com.aigirls.model.battle.ActiveMagicModel;
import com.badlogic.gdx.graphics.Color;
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
    private FilledView filledView;

    public MagicCardView(int x, int y, int cardWidth, ActiveMagicModel magic)
    {
        super(x, y, cardWidth, (int)Math.round(cardWidth/CARD_SPACE_RATE));
        ballSize = cardWidth / GameConfig.BOARD_WIDTH;
        font = BitmapFontManager.getBitmapFont(FileConfig.NYANKO_FONT_KEY);
        if (magic != null) {
            ballPlaces = magic.getConditionBallPlaces();
            canUse = magic.canOutbreak();
            magicName = magic.getName();
        } else {
            ballPlaces = new Point[0];
            canUse = true;
            magicName = "戻る";
        }
        filledView = new FilledView(leftX, lowerY, width, height, new Color(0, 0, 0, 0.8f));
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
        for (int i=0; i<ballPlaces.length; i++) {
            shapeRenderer.circle(leftX+radius+ballSize*ballPlaces[i].x, lowerY+radius+ballSize*ballPlaces[i].y, radius);
        }
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0, 0, 0, 1);
        shapeRenderer.rect(leftX, lowerY, width, height);
        shapeRenderer.line(leftX, (int)(lowerY+CARD_SPACE_RATE*height), leftX+width, (int)(lowerY+CARD_SPACE_RATE*height));
        for (int i=0; i<GameConfig.BOARD_WIDTH; i++) {
            shapeRenderer.line(leftX+ballSize*i, lowerY, leftX+ballSize*i, (int)(lowerY+CARD_SPACE_RATE*height));
        }
        shapeRenderer.end();

        batch.begin();
        font.setColor(0, 0, 0, 1);
        double spaceRateX = 0.1;
        double spaceRateY = 0.18;
        font.draw(batch, magicName, (int)(leftX+spaceRateX*width), (int)(lowerY+(CARD_SPACE_RATE+spaceRateY)*height));
        batch.end();

        if (!canUse) {
            filledView.draw(batch, shapeRenderer);
        }

    }

    public ChoiceModel getChoiceModel()
    {
        return new ChoiceModel(leftX, lowerY, width, height);
    }

}
