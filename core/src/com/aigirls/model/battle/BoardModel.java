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

    private BoardModel(BallModel[][] balls)
    {
        for (int y = 0; y < balls.length; y++) {
            for(int x = 0; x < balls[y].length; x++) {
                if (balls[y][x] == null) {
                    this.balls[y][x] = null;
                } else if (balls[y][x] instanceof ObstacleBallModel) {
                    ObstacleBallModel obstacle = (ObstacleBallModel)balls[y][x];
                    this.balls[y][x] = obstacle.getClone();
                } else {
                    this.balls[y][x] = new BallModel(balls[y][x].id);
                }
            }
        }
    }

    public BoardModel getClone()
    {
        return new BoardModel(balls);
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

    //0:ボール、1:邪魔玉
    public int[] getBallCounts(int x)
    {
        int[] counts = {0,0};
        for(int y = 0; y < balls.length; y++){
            if (balls[y][x] == null) {
                break;
            } else if (balls[y][x] instanceof ObstacleBallModel) {
                counts[1]++;
            } else {
                counts[0]++;
            }
        }
        return counts;
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

    public ObstacleBallInfoModel[] getObstacleBalls(int damage, BallInfoModel[] targets)
    {
        List<ObstacleBallInfoModel> list = new ArrayList<ObstacleBallInfoModel>();
        for (BallInfoModel target : targets) {
            list.addAll(getObstacleBallList(damage, target.x, target.y));
        }
        for (int index = 0; index < list.size(); index++) {
            ObstacleBallInfoModel target = list.get(index);
            for (int i = index+1; i < list.size();) {
                ObstacleBallInfoModel elem = list.get(i);
                if (target.id == elem.id) {
                    target.addDamage(elem.getDamage());
                    list.remove(i);
                } else {
                    i++;
                }
            }
        }
        return (ObstacleBallInfoModel[])list.toArray(new ObstacleBallInfoModel[list.size()]);
    }

    private List<ObstacleBallInfoModel> getObstacleBallList(int damage, int x, int y)
    {
        List<ObstacleBallInfoModel> list = new ArrayList<ObstacleBallInfoModel>();
        if (x+1 < GameConfig.BOARD_WIDTH && balls[y][x+1] instanceof ObstacleBallModel) {
            ObstacleBallModel obstacle = (ObstacleBallModel) balls[y][x+1];
            ObstacleBallInfoModel info = new ObstacleBallInfoModel(obstacle.id, x+1, y, obstacle.getCurrentHp(), damage);
            list.add(info);
        }
        if (0 <= x-1 && balls[y][x-1] instanceof ObstacleBallModel) {
            ObstacleBallModel obstacle = (ObstacleBallModel) balls[y][x-1];
            ObstacleBallInfoModel info = new ObstacleBallInfoModel(obstacle.id, x-1, y, obstacle.getCurrentHp(), damage);
            list.add(info);
        }
        if (y+1 < GameConfig.BOARD_HEIGHT && balls[y+1][x] instanceof ObstacleBallModel) {
            ObstacleBallModel obstacle = (ObstacleBallModel) balls[y+1][x];
            ObstacleBallInfoModel info = new ObstacleBallInfoModel(obstacle.id, x, y+1, obstacle.getCurrentHp(), damage);
            list.add(info);
        }
        if (0 <= y-1 && balls[y-1][x] instanceof ObstacleBallModel) {
            ObstacleBallModel obstacle = (ObstacleBallModel) balls[y-1][x];
            ObstacleBallInfoModel info = new ObstacleBallInfoModel(obstacle.id, x, y-1, obstacle.getCurrentHp(), damage);
            list.add(info);
        }
        return list;
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
