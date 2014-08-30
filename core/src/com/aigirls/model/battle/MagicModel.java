package com.aigirls.model.battle;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.aigirls.config.GameConfig;

public class MagicModel {
    private double attackRate;
    private double recoverRate;
    private double ballAttackRate;
    private int conditionWidth;
    private int conditionHeight;
    private Point[] conditionBallPlaces;

    public MagicModel()
    {

    }

    /**
     * 魔法発動条件の箇所を返すメソッド
     * @param places
     * @return
     */
    public List<Point[]> getTargetPlaces(boolean[][] places)
    {
        List<Point[]> targets = new ArrayList<Point[]>();
        for (int y=0; y<GameConfig.BOARD_HEIGHT-conditionHeight; y++) {
            for(int x=0; x<GameConfig.BOARD_WIDTH-conditionWidth; x++){
                if (isTargetPlace(places, x,y)) {
                    targets.add(getTargetPlace(x,y));
                }
            }
        }
        return targets;
    }

    private boolean isTargetPlace(boolean[][] places, int dx,int dy)
    {
        for (Point place : conditionBallPlaces) {
            int y = place.y+dy;
            int x = place.x+dx;
            if (y>=GameConfig.BOARD_HEIGHT && x>=GameConfig.BOARD_WIDTH && !places[y][x])
                return false;
        }
        return true;
    }

    private Point[] getTargetPlace(int dx, int dy)
    {
        Point[] places = new Point[conditionBallPlaces.length];
        for (int i=0;i<places.length;i++) {
            places[i] = new Point(conditionBallPlaces[i].x+dx, conditionBallPlaces[i].y+dy);
        }
        return places;
    }

}
