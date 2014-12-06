package com.aigirls.model.battle;

public class EnemyActionModel {
    public int[] putPlaces;
    public ActiveMagicModel magicModel;
    public BallInfoModel[] targetBalls;
    public int score;

    public EnemyActionModel(int[] places, ActiveMagicModel magic, BallInfoModel[] balls, int score)
    {
        putPlaces = places;
        magicModel = magic;
        targetBalls = balls;
        this.score = score;
    }

}
