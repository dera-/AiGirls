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
    private CharacterModel[] players;

    private int attackerIndex = -1;
    private ActiveMagicModel magic = null;
    private BallInfoModel[] targetBalls = new BallInfoModel[0];

    private BallInfoModel[] removedBalls = new BallInfoModel[0];
    private BallInfoModel[] droppedBalls = new BallInfoModel[0];
    private int damage = 0;
    private int damageToBall = 0;

    public MagicOutbreakScreen(BattleScreenView battleScreenView, CharacterModel[] players) {
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
    }

    @Override
    public void show() {
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
        getGameView().removeBalls(targetBalls, getPlayerEnum(attackerIndex));
        getGameView().removeBalls(removedBalls, getPlayerEnum(attackerIndex));
        getGameView().dropBalls(droppedBalls, getPlayerEnum(attackerIndex));
        getGameView().damageToChara(
            1.0*damage/players[(attackerIndex+1)%2].getMaxHp(),
            getPlayerEnum((attackerIndex+1)%2));
        ScreenManager.changeScreen(ScreenEnum.GameAtFinishTurn);
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
