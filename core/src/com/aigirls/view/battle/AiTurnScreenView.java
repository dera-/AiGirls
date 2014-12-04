package com.aigirls.view.battle;

import com.aigirls.config.FileConfig;
import com.aigirls.manager.BitmapFontManager;
import com.aigirls.param.battle.PlayerEnum;
import com.aigirls.screen.battle.BattleScreen;
import com.aigirls.view.GameView;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class AiTurnScreenView extends GameView implements TurnStartView{
    private BattleScreenView battleScreenView;
    private BitmapFont font;
    private boolean turnStartFlag = false;

    public AiTurnScreenView(BattleScreenView battleScreenView)
    {
        super();
        this.battleScreenView = battleScreenView;
        font = BitmapFontManager.getBitmapFont(FileConfig.NYANKO_FONT_KEY);
    }

    public BattleScreenView getBattleScreenView() {
        return battleScreenView;
    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        battleScreenView.draw(batch, shapeRenderer);
        if (turnStartFlag) {
            batch.begin();
            font.setColor(1, 1, 1, 1);
            font.draw(batch, TurnStartView.ENEMY_TURN, TurnStartView.X, TurnStartView.Y);
            batch.end();
        }
    }

    @Override
    public void display() {
        turnStartFlag = true;
        battleScreenView.setFilledAllowFlag(BattleScreen.ALLY_INDEX, true);
        battleScreenView.setFilledAllowFlag(BattleScreen.ENEMY_INDEX, true);
        battleScreenView.startBallsAnimationInStack(1, PlayerEnum.Player2);
    }

    @Override
    public void dispose() {
        turnStartFlag = false;
        battleScreenView.displayBallsnInStack(PlayerEnum.Player2);
        battleScreenView.setFilledAllowFlag(BattleScreen.ENEMY_INDEX, false);
    }

    @Override
    public void animation(float time) {
        battleScreenView.ballsAnimationInStack(time, PlayerEnum.Player2);
    }

}
