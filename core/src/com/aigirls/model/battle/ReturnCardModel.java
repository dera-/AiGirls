package com.aigirls.model.battle;

import com.badlogic.gdx.utils.Array;

public class ReturnCardModel extends ActiveMagicModel {
    public ReturnCardModel() {
        super("戻る", 0, 0, 0, null, new Array<BallInfoModel[]>());
    }

    public boolean canOutbreak() {
        return true;
    }

}
