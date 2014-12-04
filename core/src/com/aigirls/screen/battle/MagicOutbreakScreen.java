package com.aigirls.screen.battle;

import com.aigirls.manager.ScreenManager;
import com.aigirls.model.battle.ActiveMagicModel;
import com.aigirls.model.battle.BallInfoModel;
import com.aigirls.model.battle.CharacterModel;
import com.aigirls.param.ScreenEnum;
import com.aigirls.param.battle.PlayerEnum;
import com.aigirls.screen.GameScreen;
import com.aigirls.service.battle.DamageCalculateService;
import com.aigirls.view.battle.BattleScreenView;
import com.aigirls.view.battle.MagicOutbreakView;

public class MagicOutbreakScreen extends GameScreen {
    private static MagicOutbreakScreen screen;
    private static final float START_OUTBREAK_TIME = 0.05f;
    private static final float START_ATTACK_TIME = 0.85f;
    private static final float END_ATTACK_TIME = 1.2f;
    private static final float FINISH_OUTBREAK_TIME = 1.6f;

    private CharacterModel[] players;
    private int attackerIndex = -1;
    private ActiveMagicModel magic = null;
    private BallInfoModel[] targetBalls = new BallInfoModel[0];

    private BallInfoModel[] removedBalls = new BallInfoModel[0];
    private BallInfoModel[] droppedBalls = new BallInfoModel[0];
    private int damage = 0;
    private int damageToBall = 0;
    private float currentTime = 0;

    public MagicOutbreakScreen(BattleScreenView battleScreenView, CharacterModel[] players)
    {
        super(new MagicOutbreakView(battleScreenView));
        this.players = players;
    }

    public static void setMagicOutbreakScreen(BattleScreenView battleScreenView, CharacterModel[] players)
    {
        screen = new MagicOutbreakScreen(battleScreenView, players);
    }

    public static MagicOutbreakScreen getMagicOutbreakScreen()
    {
        return screen;
    }

    public void setOutbrokenMagic(int attackerIndex, ActiveMagicModel magic, BallInfoModel[] balls)
    {
        this.attackerIndex = attackerIndex;
        this.magic = magic;
        this.targetBalls = balls;
    }

    private void formatTemporaryParameters()
    {
        attackerIndex = -1;
        magic = null;
        targetBalls = new BallInfoModel[0];
        removedBalls = new BallInfoModel[0];
        droppedBalls = new BallInfoModel[0];
        damage = 0;
        damageToBall = 0;
        currentTime = 0;
    }

    @Override
    public void show() {
        getGameView().filledNothing();
        int defenderIndex = (attackerIndex+1)%2;
        damage =
            DamageCalculateService.getDamageValue(
                (int) Math.round(magic.getAttackRate() * players[attackerIndex].getAttack()),
                players[defenderIndex].getDefense());
        damageToBall = (int) Math.round(magic.getBallAttackRate() * players[attackerIndex].getMagicAttack());
        players[attackerIndex].outbreakMagic(damageToBall, targetBalls);
        removedBalls = players[attackerIndex].getRemovedBallInfoModels();
        droppedBalls = players[attackerIndex].dropBalls();
        players[defenderIndex].beHurt(damage);
        players[attackerIndex].addBallToStack(magic.getRecoverBall()); //ボール追加
    }

    @Override
    public void hide() {
        formatTemporaryParameters();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    protected void update(float delta) {
        if (isBeyondTargetTime(START_OUTBREAK_TIME, delta)) {
            getGameView().startOutbreaking(targetBalls, getPlayerEnum(attackerIndex));
        } else if (isBeyondTargetTime(START_ATTACK_TIME, delta)) {
            getGameView().outbreakInAttackerSide(removedBalls, magic.getRecoverBall(), getPlayerEnum(attackerIndex));
            getGameView().removeBalls(targetBalls, getPlayerEnum(attackerIndex));
            getGameView().outbreakInDeffenderSide(damage, getPlayerEnum((attackerIndex+1)%2));
        } else if (isBeyondTargetTime(END_ATTACK_TIME, delta)) {
            getGameView().displayBallsnInStack(getPlayerEnum(attackerIndex));
            getGameView().removeBalls(removedBalls, getPlayerEnum(attackerIndex));
            getGameView().dropBalls(droppedBalls, getPlayerEnum(attackerIndex));
        } else if (isBeyondTargetTime(FINISH_OUTBREAK_TIME, delta)) {
            ScreenManager.changeScreen(ScreenEnum.GameAtFinishTurn);
        }
        getGameView().animation(delta, getPlayerEnum(attackerIndex), getPlayerEnum((attackerIndex+1)%2));
        currentTime += delta;
    }

    private boolean isBeyondTargetTime(float targetTime, float delta)
    {
        return currentTime < targetTime && targetTime <= currentTime+delta;
    }

    protected MagicOutbreakView getGameView() {
        return (MagicOutbreakView)view;
    }

    private PlayerEnum getPlayerEnum(int index)
    {
        if (index%2 == BattleScreen.ALLY_INDEX) {
            return PlayerEnum.Player1;
        } else {
            return PlayerEnum.Player2;
        }
    }

}
