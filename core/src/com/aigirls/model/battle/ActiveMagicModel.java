package com.aigirls.model.battle;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.aigirls.config.GameConfig;
import com.aigirls.model.MagicOutbreakModel;

public class ActiveMagicModel {
    private String name;
    private double attackRate;
    private double ballAttackRate;
    private int recoverBall;
    private MagicOutbreakModel outbreak;
    private Array<BallInfoModel[]> targetBalls;


    public ActiveMagicModel(
        String name,
        double attackRate,
        double ballAttackRate,
        int recoverBall,
        MagicOutbreakModel outbreak,
        Array<BallInfoModel[]> targetBalls)
    {
        this.name = name;
        this.attackRate = attackRate;
        this.ballAttackRate = ballAttackRate;
        this.recoverBall    = recoverBall;
        this.outbreak = outbreak;
        this.targetBalls = targetBalls;
    }

    public String getName() {
        return name;
    }

    public double getAttackRate(){
        return attackRate;
    }

    public double getBallAttackRate() {
        return ballAttackRate;
    }

    public boolean canOutbreak() {
        return targetBalls.size > 0;
    }

    public int getRecoverBall() {
        return recoverBall;
    }

    public Vector2[] getConditionBallPlaces() {
        if (outbreak == null) {
            return new Vector2[0];
        }
        Vector2[] ballPlaces = outbreak.getConditionBallPlaces();
        Vector2[] normalizedBallPlaces = new Vector2[ballPlaces.length];
        int width = outbreak.getConditionWidth();
        for (int i=0; i<normalizedBallPlaces.length; i++) {
            normalizedBallPlaces[i] = new Vector2(ballPlaces[i].x+(GameConfig.BOARD_WIDTH-width)/2, ballPlaces[i].y);
        }
        return normalizedBallPlaces;
    }

    public int getNumTargetBalls() {
        return targetBalls.size;
    }

    public BallInfoModel[] getBallInfoModels(int index) {
        return targetBalls.get(index);
    }

    public double getStrength() {
        return getStrength(1.25, 0.8, 0.6);
    }

    public double getStrength(double attackWeight, double ballAttackWeight, double recoverWeight) {
        if (outbreak == null) {
            return 0;
        }
        return (attackWeight*attackRate + ballAttackWeight*ballAttackRate + recoverWeight*recoverBall);
    }

}
