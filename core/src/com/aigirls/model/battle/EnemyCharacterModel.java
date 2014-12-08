package com.aigirls.model.battle;

import com.aigirls.entity.CharacterEntity;

public class EnemyCharacterModel extends CharacterModel {
    public static final int CAN_NOT_PUT_BALL = -1;

    private EnemyBrainModel brain;

    public EnemyCharacterModel(CharacterEntity entity, MagicModel[] magics) {
        super(entity, magics);
        brain = new EnemyBrainModel();
    }

    public EnemyActionModel getAction(CharacterModel player, int totalBallCount)
    {
        brain.settTotalBallCount(totalBallCount);
        return brain.decideAction(this, player);
    }

}
