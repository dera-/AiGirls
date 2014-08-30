package com.aigirls.model.battle;

import com.aigirls.config.GameConfig;

public class BoardModel {
    private static final int CAN_NOT_SET_BALL = -1;
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

    private int getDropPlace(int x)
    {
        for(int y = 0; y < balls.length; y++){
            if (balls[y][x] == null) return y;
        }
        return CAN_NOT_SET_BALL;
    }

}
