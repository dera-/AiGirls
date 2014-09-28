package com.aigirls.model.battle;

import java.util.ArrayList;
import java.util.List;

import com.aigirls.config.GameConfig;

public class BoardModel {
    public static final int CAN_NOT_SET_BALL = -1;
    private BallModel[][] balls = new BallModel[GameConfig.BOARD_HEIGHT][GameConfig.BOARD_WIDTH];

    public BoardModel()
    {
        formatBoard();
    }

    private void formatBoard()
    {
        for(int y = 0; y < balls.length; y++)
            for(int x = 0; x < balls[y].length; x++)
                balls[y][x] = null;
    }

    public boolean isExistPlaceToPut()
    {
        for (int x=0; x<GameConfig.BOARD_WIDTH; x++) {
            if (canSetBall(x)) return true;
        }
        return false;
    }

    /**
     * ボールを引数の場所に置く
     * @param x
     * @param ball
     * @return boolean ボールを置けるかどうか
     */
    public boolean setBall(int x, BallModel ball)
    {
        int y = getDropPlace(x);
        if(y == CAN_NOT_SET_BALL) return false;
        balls[y][x] = ball;
        return true;
    }

    /**
     * 引数の場所にボールを置けるかどうか
     * @param x
     * @return boolean
     */
    public boolean canSetBall(int x)
    {
        return balls[GameConfig.BOARD_HEIGHT-1][x]==null;
    }

    public int getDropPlace(int x)
    {
        for(int y = 0; y < balls.length; y++){
            if (balls[y][x] == null) return y;
        }
        return CAN_NOT_SET_BALL;
    }

    public boolean removeBall(int id)
    {
        for (int y = 0; y < balls.length; y++) {
            for (int x = 0; x < balls[y].length; x++) {
                if (balls[y][x] != null && balls[y][x].id == id) {
                    balls[y][x] = null;
                    return true;
                }
            }
        }
        return false;
    }

    public void outbreakMagic(int damage, BallInfoModel[] targets)
    {
        for (BallInfoModel target : targets) {
            balls[target.y][target.x] = null;
            attackToObstacleBall(damage, target.x, target.y);
        }
    }

    private void attackToObstacleBall(int damage, int x, int y)
    {
        if (x+1 < GameConfig.BOARD_WIDTH && balls[y][x+1] instanceof ObstacleBallModel) {
            ObstacleBallModel obstacle = (ObstacleBallModel) balls[y][x+1];
            obstacle.beHurt(damage);
        }
        if (0 <= x-1 && balls[y][x-1] instanceof ObstacleBallModel) {
            ObstacleBallModel obstacle = (ObstacleBallModel) balls[y][x-1];
            obstacle.beHurt(damage);
        }
        if (y+1 < GameConfig.BOARD_HEIGHT && balls[y+1][x] instanceof ObstacleBallModel) {
            ObstacleBallModel obstacle = (ObstacleBallModel) balls[y+1][x];
            obstacle.beHurt(damage);
        }
        if (0 <= y-1 && balls[y-1][x] instanceof ObstacleBallModel) {
            ObstacleBallModel obstacle = (ObstacleBallModel) balls[y-1][x];
            obstacle.beHurt(damage);
        }
    }

    public BallInfoModel[] getRemovedBallInfoModels()
    {
        List<BallInfoModel> list = new ArrayList<BallInfoModel>();
        for (int y=0; y<balls.length; y++) {
            for (int x=0; x<balls[y].length; x++) {
                if (balls[y][x] instanceof ObstacleBallModel && ((ObstacleBallModel) balls[y][x]).isBroken()) {
                    list.add(new BallInfoModel(balls[y][x].id, x, y));
                    balls[y][x] = null;
                }
            }
        }
        return (BallInfoModel[])list.toArray(new BallInfoModel[list.size()]);
    }

    public BallInfoModel[] dropBalls()
    {
        List<BallInfoModel> list = new ArrayList<BallInfoModel>();
        for (int x=0; x<GameConfig.BOARD_WIDTH; x++) {
            int distance = 0;
            for (int y=0; y<GameConfig.BOARD_HEIGHT; y++) {
                if (balls[y][x] == null) {
                    distance++;
                } else if (distance > 0) {
                    BallModel target = balls[y][x];
                    balls[y-distance][x] = target;
                    balls[y][x] = null;
                    list.add(new BallInfoModel(target.id, x, y-distance));
                }
            }
        }
        return (BallInfoModel[])list.toArray(new BallInfoModel[list.size()]);
    }

    public BallModel[][] getBalls()
    {
        return balls;
    }

}
