package com.aigirls.view;

import com.aigirls.config.FileConfig;
import com.aigirls.manager.BitmapFontManager;
import com.aigirls.manager.TextureManager;
import com.aigirls.model.ChoiceModel;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class ButtomView extends GameView {
    private Color buttomColor = null;
    private Color fontColor = null;
    private boolean canPush = true;
    private String name;
    private ChoiceModel choiceModel;
    private BitmapFont font;
    private Sprite buttomSprite;
    private FilledView filledView;

    public ButtomView(int x, int y, int w, int h, String name)
    {
        this(x, y, w, h, name, null, null);
    }

    public ButtomView(int x, int y, int w, int h, String name, Color buttomColor, Color fontColor)
    {
        super(x, y, w, h);
        this.name = name;
        this.choiceModel = new ChoiceModel(leftX, lowerY, width, height);
        this.buttomColor = buttomColor;
        this.fontColor = fontColor;
        this.choiceModel = new ChoiceModel(x, y, w, h);
        this.font = BitmapFontManager.getBitmapFont(FileConfig.NYANKO_FONT_KEY);
        this.buttomSprite = new Sprite(TextureManager.getTexture(FileConfig.BUTTOM_KEY));
        this.filledView = new FilledView(x, y, w, h, new Color(0, 0, 0, 0.7f));
    }

    public ChoiceModel getChoiceModel()
    {
        return choiceModel;
    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        batch.begin();
        if (buttomColor != null) {
            buttomSprite.setColor(buttomColor);
        }
        buttomSprite.setPosition(leftX, lowerY);
        buttomSprite.setSize(width, height);
        buttomSprite.draw(batch);

        if (fontColor != null) {
            font.setColor(fontColor);
        }
        double spaceRateX = 0.1;
        double spaceRateY = 0.2;
        font.draw(batch, name, (int)Math.round(leftX+spaceRateX*width), (int)Math.round(lowerY+(1-spaceRateY)*height));
        batch.end();

        if (!canPush) {
            filledView.draw(batch, shapeRenderer);
        }
    }

    public void setCanPush(boolean pushFlag)
    {
        canPush = pushFlag;
    }

    public boolean canPush()
    {
        return canPush;
    }

}
