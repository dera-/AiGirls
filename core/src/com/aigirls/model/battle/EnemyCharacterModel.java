package com.aigirls.model.battle;

import java.util.ArrayList;
import java.util.List;

import com.aigirls.config.GameConfig;
import com.aigirls.entity.CharacterEntity;

public class EnemyCharacterModel extends CharacterModel {
    public static final int CAN_NOT_PUT_BALL = -1;

    private ActiveMagicModel magicToUse = null;
    private BallInfoModel[] ballsToUse = new BallInfoModel[0];

    public EnemyCharacterModel(CharacterEntity entity, MagicModel[] magics) {
        super(entity, magics);
    }

    public int decidePutPlace(CharacterModel playerChara){
        //とりあえずランダムで取得
        List<Integer> canPutPlaces = new ArrayList<Integer>();
        for (int x=0; x<GameConfig.BOARD_WIDTH; x++) {
            if (board.canSetBall(x)) {
                canPutPlaces.add(x);
            }
        }
        if (canPutPlaces.size() == 0) {
            return CAN_NOT_PUT_BALL;
        }
        int index = (int) (Math.random() * canPutPlaces.size());
        return canPutPlaces.get(index);
    }

    public void useMagic(CharacterModel playerChara){
        ActiveMagicModel[] magics = getActiveMagicModels();
        for (ActiveMagicModel m : magics) {
            if (m.canOutbreak() && (magicToUse == null || m.getStrength() > magicToUse.getStrength())) {
                magicToUse = m;
                ballsToUse = m.getBallInfoModels(0);
            }
        }
    }

    public ActiveMagicModel getMagicToUse(){
        return magicToUse;
    }

    public BallInfoModel[] getBallsToUse(){
        return ballsToUse;
    }

    public void formatTemporaryParameters(){
        magicToUse = null;
        ballsToUse = new BallInfoModel[0];
    }

}
