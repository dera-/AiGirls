package com.aigirls.model.battle;

import com.aigirls.config.GameConfig;

public class BallStackModel {
    private int currentBallCount;

    public BallStackModel() {
        currentBallCount = GameConfig.DEFAULT_STACKED_BALL_NUM;
    }

    public double getStatusRate() {
        return GameConfig.STATUS_RATES[currentBallCount];
    }

    public boolean addBall() {
        return changeBallCount(1);
    }

    public boolean addBall(int num) {
        return changeBallCount(num);
    }

    public boolean removeBall() {
        System.out.println("減少："+(currentBallCount-1));
        return changeBallCount(-1);
    }

    public boolean isExistBall() {
        System.out.println(currentBallCount);
        return currentBallCount > 0;
    }

    private boolean changeBallCount(int num) {
        if (currentBallCount + num < 0 || currentBallCount + num > GameConfig.LIMIT_STACKED_BALL_NUM) {
            return false;
        }
        currentBallCount += num;
        return true;
    }

}
