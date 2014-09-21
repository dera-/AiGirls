package com.aigirls.view.battle;

import com.aigirls.config.GameConfig;
import com.aigirls.view.FilledView;
import com.aigirls.view.VerticallyLongMenuView;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class ActionSelectView extends VerticallyLongMenuView{
    public static final int MAGIC_ATTACK = 2;
    public static final int TURN_END = 1;
    public static final int RESET = 0;
    private static final int ACTION_NUMS = 3;
    private static String[] choiceItems = new String[ACTION_NUMS];
    private BattleScreenView battleScreenView;
    private FilledView filledView;

    static{
        choiceItems[0] = "戻る";
        choiceItems[1] = "ターン終了";
        choiceItems[2] = "使用魔法選択";
    }

    public ActionSelectView(BattleScreenView battleScreenView)
    {
        super(
                (int)Math.round(0.3*GameConfig.GAME_WIDTH),
                (int)Math.round(0.3*GameConfig.GAME_HEIGHT),
                (int)Math.round(0.4*GameConfig.GAME_WIDTH),
                (int)Math.round(0.4*GameConfig.GAME_HEIGHT),
                choiceItems
                );
        this.battleScreenView = battleScreenView;
        filledView = new FilledView(0, 0, GameConfig.GAME_WIDTH, GameConfig.GAME_HEIGHT, new Color(0, 0, 0, 0.8f));
    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer)
    {
        //バトル画面の描画
        battleScreenView.draw(batch, shapeRenderer);
        //全体塗りつぶし
        filledView.draw(batch, shapeRenderer);
        //選択肢メニューの描画
        super.draw(batch, shapeRenderer);
    }

}
