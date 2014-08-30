package com.aigirls.model.battle;

public class ObstacleBallModel extends BallModel{
    protected final int hp;
    protected int nowHp;

    public ObstacleBallModel(int id, int hp)
    {
        super(id);
        this.hp = hp;
        this.nowHp = hp;
    }

    public boolean isBroken()
    {
        return this.nowHp > 0;
    }
}
