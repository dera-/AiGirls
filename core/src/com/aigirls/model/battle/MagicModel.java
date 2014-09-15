package com.aigirls.model.battle;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.aigirls.config.GameConfig;
import com.aigirls.entity.MagicEntity;
import com.aigirls.io.file.OutbreakFileReader;
import com.aigirls.model.MagicOutbreakModel;

public class MagicModel {
    private String name;
    private double attackRate;
    private double ballAttackRate;
    private MagicOutbreakModel outbreakModel;

    public MagicModel(MagicEntity entity)
    {
        name = entity.name;
        attackRate = entity.attackRate;
        ballAttackRate = entity.magicAttackRate;
        outbreakModel = OutbreakFileReader.getMagicOutbreakModel(entity.outbreakFilePath);
    }

    public ActiveMagicModel generateActiveMagicModel(BallModel[][] balls) {
        return new ActiveMagicModel(name, attackRate, ballAttackRate, outbreakModel, getTargetBallsInfo(balls));
    }

    /**
     * 魔法発動条件の箇所を返すメソッド
     * @param balls
     * @return
     */
    private List<BallInfoModel[]> getTargetBallsInfo(BallModel[][] balls)
    {
        List<BallInfoModel[]> targets = new ArrayList<BallInfoModel[]>();
        for (int y=0; y<GameConfig.BOARD_HEIGHT-outbreakModel.getConditionHeight(); y++) {
            for(int x=0; x<GameConfig.BOARD_WIDTH-outbreakModel.getConditionWidth(); x++){
                BallInfoModel[] target = getTargetBallInfo(balls, x, y);
                if (target != null) {
                    targets.add(target);
                }
            }
        }
        return targets;
    }

    private BallInfoModel[] getTargetBallInfo(BallModel[][] balls,int dx, int dy)
    {
        Point[] conditionBallPlaces = outbreakModel.getConditionBallPlaces();
        BallInfoModel[] target = new BallInfoModel[conditionBallPlaces.length];
        for (int i=0;i<target.length;i++) {
            int y = conditionBallPlaces[i].y+dy;
            int x = conditionBallPlaces[i].x+dx;
            if (y>=GameConfig.BOARD_HEIGHT
                || x>=GameConfig.BOARD_WIDTH
                || balls[y][x] == null
                || balls[y][x] instanceof ObstacleBallModel) {
                return null;
            }
            target[i] = new BallInfoModel(balls[y][x].id, x, y);
        }
        return target;
    }

}
