package com.aigirls.view.battle;

import com.aigirls.config.GameConfig;
import com.aigirls.model.battle.BallInfoModel;
import com.aigirls.param.battle.PlayerEnum;
import com.aigirls.screen.battle.BattleScreen;
import com.aigirls.view.GameView;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class MagicOutbreakView extends GameView {
    private BattleScreenView battleScreenView;

    public MagicOutbreakView(BattleScreenView battleScreenView)
    {
        super(0, 0, GameConfig.GAME_WIDTH, GameConfig.GAME_HEIGHT);
        this.battleScreenView = battleScreenView;
    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        battleScreenView.draw(batch, shapeRenderer);
        //TODO 魔法名とかエフェクトとかの描画
    }

    public void removeBalls(BallInfoModel[] ballsInfo, PlayerEnum player)
    {
        battleScreenView.removeBalls(ballsInfo, player);
    }

    public void dropBalls(BallInfoModel[] ballsInfo, PlayerEnum player)
    {
        battleScreenView.dropBalls(ballsInfo, player);
    }

    public void damageToChara(int damage, PlayerEnum player)
    {
        battleScreenView.moveHpBar(damage, player);
    }

    public void recoverBall(int num, PlayerEnum player)
    {
        for (int i = 0; i<num; i++) {
            battleScreenView.addToBallStack(player);
        }
    }

    public void filledNothing () {
        this.battleScreenView.setFilledAllowFlag(BattleScreen.ALLY_INDEX, false);
        this.battleScreenView.setFilledAllowFlag(BattleScreen.ENEMY_INDEX, false);
    }

}
