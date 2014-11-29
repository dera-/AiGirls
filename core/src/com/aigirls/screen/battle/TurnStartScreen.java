package com.aigirls.screen.battle;

import com.aigirls.model.battle.BallModel;
import com.aigirls.model.battle.BoardModel;
import com.aigirls.model.battle.CharacterModel;
import com.aigirls.model.battle.ObstacleBallModel;
import com.aigirls.param.battle.PlayerEnum;
import com.aigirls.screen.GameScreen;
import com.aigirls.service.battle.DamageCalculateService;
import com.aigirls.view.GameView;
import com.aigirls.view.battle.BattleScreenView;

public abstract class TurnStartScreen extends GameScreen
{
    protected final int totalBallCount;
    protected CharacterModel attacker;
    protected CharacterModel defender;
    protected PlayerEnum attackerEnum;
    protected PlayerEnum defenderEnum;
    protected int droppedBallNums = 0;
    protected int damageByDroppingBall = 0;

    public TurnStartScreen(GameView view, CharacterModel attacker, CharacterModel defender, PlayerEnum attackerEnum, int totalBallCount)
    {
        super(view);
        this.totalBallCount = totalBallCount;
        this.attacker = attacker;
        this.defender = defender;
        this.attackerEnum = attackerEnum;
        if (attackerEnum == PlayerEnum.Player1) {
            this.defenderEnum = PlayerEnum.Player2;
        } else {
            this.defenderEnum = PlayerEnum.Player1;
        }
        //スタックへのボールの補充
        attacker.addBallToStack(1);
        getBattleScreenView().addToBallStack(attackerEnum);
    }

    @Override
    public void show() {
        getBattleScreenView().filledDefenderView(getIndex(defenderEnum));
    }

    public int getDroppedBallNums() {
        return droppedBallNums;
    }

    protected void dropBallEvent(int xPlace, int yPlace)
    {
        int ballId = totalBallCount + droppedBallNums;
        attacker.setBall(xPlace, new BallModel(ballId));
        getBattleScreenView().addBall(ballId, xPlace, yPlace, attackerEnum);
        int defenserYPlace = defender.getDropPlace(xPlace);
        if (defenserYPlace == BoardModel.CAN_NOT_SET_BALL) {
            int damage = DamageCalculateService.getDamageValue(
                    attacker.getAttack(),
                    defender.getDefense());
            defender.beHurt(damage);
            getBattleScreenView().moveHpBar(damage, defenderEnum);
            damageByDroppingBall += damage;
        } else {
            defender.setBall(
                xPlace,
                new ObstacleBallModel(ballId, attacker.getMagicDefense()));
            getBattleScreenView().addObstacle(ballId, xPlace, defenserYPlace, defenderEnum);
        }
        droppedBallNums++;
    }

    protected abstract BattleScreenView getBattleScreenView();

    private int getIndex(PlayerEnum player) {
        switch(player){
            case Player1:
                return BattleScreen.ALLY_INDEX;
            case Player2:
                return BattleScreen.ENEMY_INDEX;
            default:
                return -1;
        }
    }
}
