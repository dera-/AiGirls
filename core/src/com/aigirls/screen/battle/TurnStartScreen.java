package com.aigirls.screen.battle;

import com.aigirls.model.battle.BallModel;
import com.aigirls.model.battle.BoardModel;
import com.aigirls.model.battle.CharacterModel;
import com.aigirls.model.battle.ObstacleBallModel;
import com.aigirls.param.battle.PlayerEnum;
import com.aigirls.screen.GameScreen;
import com.aigirls.service.battle.DamageCalculateService;
import com.aigirls.view.CharacterView;
import com.aigirls.view.GameView;
import com.aigirls.view.battle.BattleScreenView;

public abstract class TurnStartScreen extends GameScreen
{
    public static final float TIME = 1f;

    protected final int totalBallCount;
    protected CharacterModel attacker;
    protected CharacterModel defender;
    protected PlayerEnum attackerEnum;
    protected PlayerEnum defenderEnum;
    protected int droppedBallNums = 0;
    protected int damageByDroppingBall = 0;
    protected float currentTime = 0;

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

        //表情リセット
        getBattleScreenView().changeCharaExpression(CharacterView.EXPRESSION_NORMAL, PlayerEnum.Player1);
        getBattleScreenView().changeCharaExpression(CharacterView.EXPRESSION_NORMAL, PlayerEnum.Player2);

        //アニメーション開始
        startAnimation();
    }

    @Override
    public void show()
    {
        //getBattleScreenView().filledDefenderView(getIndex(defenderEnum));
    }

    protected void update(float delta)
    {
        if (currentTime < TIME && TIME <= currentTime+delta) {
            endAnimation();
            currentTime += delta;
            return;
        } else if (currentTime+delta < TIME) {
            animation(delta);
            currentTime += delta;
            return;
        }
        action(delta);
    }

    protected abstract void action(float delta);

    public int getDroppedBallNums() {
        return droppedBallNums;
    }

    protected void dropBallEvent(int xPlace, int yPlace)
    {
        droppedBallNums++;
        int ballId = totalBallCount + droppedBallNums;
        attacker.removeBallFromStack();
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

    protected abstract void startAnimation();
    protected abstract void animation(float time);
    protected abstract void endAnimation();
}
