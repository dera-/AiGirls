package com.aigirls.view.battle;

import com.aigirls.config.GameConfig;
import com.aigirls.model.ChoiceListModel;
import com.aigirls.view.GameView;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class ActionSelectView extends GameView{
    public static final int MAGIC_ATTACK = 0;
    public static final int TURN_END = 1;
    private static final int ACTION_NUMS = 2;
    private ChoiceListModel choiceList;
    private final int leftX;
    private final int lowY;
    private final int width;
    private final int height;
    private final int interval;
    private BattleScreenView battleScreenView;
    private BitmapFont font;

    public ActionSelectView(BattleScreenView battleScreenView)
    {
        leftX = (int)Math.round(0.3*GameConfig.GAME_WIDTH);
        lowY = (int)Math.round(0.3*GameConfig.GAME_HEIGHT);
        width = (int)Math.round(0.4*GameConfig.GAME_WIDTH);
        height = (int)Math.round(0.4*GameConfig.GAME_HEIGHT);
        interval = (int)Math.round(1.0*width/ACTION_NUMS);

        choiceList = new ChoiceListModel(
                leftX,
                lowY,
                width,
                height,
                ACTION_NUMS,
                0,
                interval);
        this.battleScreenView = battleScreenView;
        font = new BitmapFont();
    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer)
    {
        //バトル画面の描画
        battleScreenView.draw(batch, shapeRenderer);

        //全体塗りつぶし
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.2f, 0.4f, 0.8f, 0.8f);
        shapeRenderer.rect(leftX, lowY, width, height);
        shapeRenderer.end();
        //枠の描画
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0, 0, 0, 1);
        shapeRenderer.rect(leftX, lowY, width, height);
        shapeRenderer.line(leftX, lowY+interval, leftX+width, lowY+interval);
        shapeRenderer.end();
        //選択肢の描画
        batch.begin();
        font.setScale(20f);
        font.setColor(1, 1, 1, 1);
        font.draw(batch, "魔法カード使用", leftX, lowY+interval);
        font.draw(batch, "ターン終了", leftX, lowY);
        batch.end();
    }

    public int getChoicedPlace(int x, int y)
    {
        return choiceList.getChoicedPlace(x, y);
    }

}
