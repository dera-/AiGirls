package com.aigirls.view;

import com.aigirls.config.FileConfig;
import com.aigirls.config.GameConfig;
import com.aigirls.manager.BitmapFontManager;
import com.aigirls.model.ChoiceModel;
import com.aigirls.model.battle.ActiveMagicModel;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class MagicCardView extends GameView {
    private static final double CARD_SPACE_RATE = 0.8;
    private int ballSize;
    private ActiveMagicModel magicInfo;
    private BitmapFont font;
    private FilledView filledView;
    private Vector2 cardPlace;

    public MagicCardView(int x, int y, int cardWidth, ActiveMagicModel magic)
    {
        super(x, y, cardWidth, (int)Math.round(cardWidth/CARD_SPACE_RATE));
        ballSize = cardWidth / GameConfig.BOARD_WIDTH;
        font = BitmapFontManager.getBitmapFont(FileConfig.NYANKO_FONT_KEY);
        magicInfo = magic;
        filledView = new FilledView(leftX, lowerY, width, height, new Color(0, 0, 0, 0.8f));
        cardPlace = new Vector2(x, y);
    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer)
    {
        int leftX = (int)cardPlace.x;
        int lowerY = (int)cardPlace.y;
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1, 1, 1, 1);
        shapeRenderer.rect(leftX, lowerY, width, height);
        //ボールの描画
        int radius = ballSize/2;
        shapeRenderer.setColor(0.1f, 0.75f, 0.4f, 1);
        Vector2[] ballPlaces = magicInfo.getConditionBallPlaces();
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
        font.draw(batch, magicInfo.getName(), (int)(leftX+spaceRateX*width), (int)(lowerY+(CARD_SPACE_RATE+spaceRateY)*height));
        batch.end();

        if (!magicInfo.canOutbreak()) {
            filledView.draw(batch, shapeRenderer);
        }
    }

    public String getMagicName()
    {
        return magicInfo.getName();
    }

    public String displayUsableFlag()
    {
        return magicInfo.canOutbreak() ? "使用可能!!" : "使用不可能...";
    }

    public int getAttackPercent()
    {
        return (int) Math.round(100*magicInfo.getAttackRate());
    }

    public int getBallAttackPercent()
    {
        return (int) Math.round(100*magicInfo.getBallAttackRate());
    }

    public int getRecoverBall()
    {
        return magicInfo.getRecoverBall();
    }

    public ChoiceModel getChoiceModel()
    {
        return new ChoiceModel(leftX, lowerY, width, height);
    }

    public void setCardPlace(int x, int y)
    {
        cardPlace.set(x, y);
        filledView.setPlace(x, y);
    }

    public void resetCardPlace()
    {
        cardPlace.set(leftX, lowerY);
        filledView.setDefaultPlace();
    }

    public Vector2 getCardPlace()
    {
        return cardPlace;
    }

    public ActiveMagicModel getMagicInfo()
    {
        return magicInfo;
    }

}
