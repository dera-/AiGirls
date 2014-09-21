package com.aigirls.view;

import com.aigirls.config.FileConfig;
import com.aigirls.manager.BitmapFontManager;
import com.aigirls.model.ChoiceListModel;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class VerticallyLongMenuView extends SelectView {
    private String[] menuItems;
    private int interval;
    private BitmapFont font;

    public VerticallyLongMenuView(int x, int y, int w, int h, String[] items) {
        super(x, y, w, h, items.length);
        menuItems = items;
        interval = (int)Math.round(1.0*height/choiceItemNums);
        font = BitmapFontManager.getBitmapFont(FileConfig.NYANKO_FONT_KEY);
    }

    @Override
    protected ChoiceListModel getChoiceListModel() {
        return  new ChoiceListModel(
                leftX,
                lowerY,
                width,
                height,
                choiceItemNums,
                0,
                (int)Math.round(1.0*height/choiceItemNums)
        );
    }

    public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer, Color backgroundColor, Color stringColor) {
        //塗りつぶし
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(backgroundColor);
        shapeRenderer.rect(leftX, lowerY, width, height);
        shapeRenderer.end();
        //枠の描画
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(stringColor);
        shapeRenderer.rect(leftX, lowerY, width, height);
        for (int i=1; i<choiceItemNums; i++) {
            shapeRenderer.line(leftX, lowerY+interval*i, leftX+width, lowerY+interval*i);
        }
        shapeRenderer.end();
        //選択肢の描画
        batch.begin();
        font.setColor(stringColor);
        int leftSpace = (int)Math.round(0.15*width);
        int lowerSpace = (int)Math.round(0.2*height);
        for (int i=0; i < menuItems.length; i++) {
            font.draw(batch, menuItems[i], leftX+leftSpace, lowerY+lowerSpace+i*interval);
        }
        batch.end();
    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        draw(batch, shapeRenderer, new Color(0,0,0,1), new Color(1,1,1,1));
    }

}
