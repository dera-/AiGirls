package com.aigirls.model.battle;

import java.awt.Point;
import java.util.List;

import com.aigirls.config.GameConfig;
import com.aigirls.model.MagicOutbreakModel;

public class ActiveMagicModel {
    private String name;
    private double attackRate;
    private double ballAttackRate;
    private MagicOutbreakModel outbreak;
    private List<BallInfoModel[]> targetBalls;


    public ActiveMagicModel(
        String name,
        double attackRate,
        double ballAttackRate,
        MagicOutbreakModel outbreak,
        List<BallInfoModel[]> targetBalls)
    {
        this.name = name;
        this.attackRate = attackRate;
        this.ballAttackRate = ballAttackRate;
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
        return targetBalls.size() > 0;
    }

    public Point[] getConditionBallPlaces() {
        Point[] ballPlaces = outbreak.getConditionBallPlaces();
        Point[] normalizedBallPlaces = new Point[ballPlaces.length];
        int width = outbreak.getConditionWidth();
        for (int i=0; i<normalizedBallPlaces.length; i++) {
            normalizedBallPlaces[i] = new Point(ballPlaces[i].x+(GameConfig.BOARD_WIDTH-width)/2, ballPlaces[i].y);
        }
        return normalizedBallPlaces;
    }

    public int getNumTargetBalls() {
        return targetBalls.size();
    }

    public BallInfoModel[] getBallInfoModels(int index) {
        return targetBalls.get(index);
    }

    public double getStrength() {
        return getStrength(1, 1);
    }

    public double getStrength(double attackWeight, double ballAttackWeight) {
        int ballNums = outbreak.getConditionBallPlaces().length;
        return (attackWeight*attackRate + ballAttackWeight*ballAttackRate) / ballNums;
    }

}
