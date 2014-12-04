package com.aigirls.view.battle;

import com.aigirls.config.GameConfig;
import com.aigirls.model.battle.BallInfoModel;
import com.aigirls.param.battle.PlayerEnum;
import com.aigirls.screen.battle.BattleScreen;
import com.aigirls.view.CharacterView;
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
    }

    public void animation(float time, PlayerEnum attacker, PlayerEnum deffender)
    {
        battleScreenView.ballsAnimationInBoard(time, attacker);
        battleScreenView.ballsAnimationInStack(time, attacker);
        battleScreenView.attackAnimation(time, deffender);
    }

    public void startOutbreaking(BallInfoModel[] targetBalls, PlayerEnum player)
    {
        battleScreenView.startBallsAnimationInBoard(targetBalls, player);
        battleScreenView.changeCharaExpression(CharacterView.EXPRESSION_ATTACK, player);
    }

    public void outbreakInAttackerSide(BallInfoModel[] brokenBalls, int recoverBallNum, PlayerEnum player)
    {
        battleScreenView.startBallsAnimationInBoard(brokenBalls, player);
        for (int i = 0; i<recoverBallNum; i++) {
            battleScreenView.addToBallStack(player);
        }
        battleScreenView.startBallsAnimationInStack(recoverBallNum, player);
    }

    public void outbreakInDeffenderSide(int damage, PlayerEnum player)
    {
        battleScreenView.startAttackAnimation(player);
        battleScreenView.moveHpBar(damage, player);
        battleScreenView.changeCharaExpression(CharacterView.EXPRESSION_DAMEGE, player);
    }

    public void removeBalls(BallInfoModel[] ballsInfo, PlayerEnum player)
    {
        battleScreenView.removeBalls(ballsInfo, player);
    }

    public void dropBalls(BallInfoModel[] ballsInfo, PlayerEnum player)
    {
        battleScreenView.dropBalls(ballsInfo, player);
    }

    public void displayBallsnInStack(PlayerEnum player)
    {
        battleScreenView.displayBallsnInStack(player);
    }

    public void filledNothing () {
        this.battleScreenView.setFilledAllowFlag(BattleScreen.ALLY_INDEX, false);
        this.battleScreenView.setFilledAllowFlag(BattleScreen.ENEMY_INDEX, false);
    }

}
