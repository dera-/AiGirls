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
        int width = outbreak.getConditionWidth();
        for (int i=0; i<ballPlaces.length; i++) {
            ballPlaces[i].x += (GameConfig.BOARD_WIDTH-width)/2;
        }
        return ballPlaces;
    }

    public int getNumTargetBalls() {
        return targetBalls.size();
    }

    public BallInfoModel[] getBallInfoModels(int index) {
        return targetBalls.get(index);
    }

}
