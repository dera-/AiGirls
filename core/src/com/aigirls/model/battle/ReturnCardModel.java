package com.aigirls.model.battle;

import java.util.ArrayList;


public class ReturnCardModel extends ActiveMagicModel {
    public ReturnCardModel() {
        super("戻る", 0, 0, 0, null, new ArrayList<BallInfoModel[]>());
    }

    public boolean canOutbreak() {
        return true;
    }

}
