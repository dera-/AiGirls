package com.aigirls.model.battle;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.aigirls.config.GameConfig;
import com.aigirls.entity.MagicEntity;
import com.aigirls.io.file.OutbreakFileReader;
import com.aigirls.model.MagicOutbreakModel;

public class MagicModel {
    private String name;
    private double attackRate;
    private double ballAttackRate;
    private int recoverBall;
    private MagicOutbreakModel outbreakModel;

    public MagicModel(MagicEntity entity)
    {
        name = entity.name;
        attackRate = entity.attackRate;
        ballAttackRate = entity.magicAttackRate;
        recoverBall = entity.recoverBall;
        outbreakModel = OutbreakFileReader.getMagicOutbreakModel(entity.outbreakFilePath);
    }

    public ActiveMagicModel generateActiveMagicModel(BallModel[][] balls) {
        return new ActiveMagicModel(name, attackRate, ballAttackRate, recoverBall, outbreakModel, getTargetBallsInfo(balls));
    }

    /**
     * 魔法発動条件の箇所を返すメソッド
     * @param balls
     * @return
     */
    private Array<BallInfoModel[]> getTargetBallsInfo(BallModel[][] balls)
    {
        Array<BallInfoModel[]> targets = new Array<BallInfoModel[]>();
        for (int y=0; y<=GameConfig.BOARD_HEIGHT-outbreakModel.getConditionHeight(); y++) {
            for(int x=0; x<=GameConfig.BOARD_WIDTH-outbreakModel.getConditionWidth(); x++){
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
        Vector2[] conditionBallPlaces = outbreakModel.getConditionBallPlaces();
        BallInfoModel[] target = new BallInfoModel[conditionBallPlaces.length];
        for (int i=0;i<target.length;i++) {
            int y = (int)conditionBallPlaces[i].y + dy;
            int x = (int)conditionBallPlaces[i].x + dx;
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
