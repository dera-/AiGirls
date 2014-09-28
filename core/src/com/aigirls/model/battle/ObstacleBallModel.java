package com.aigirls.model.battle;

public class ObstacleBallModel extends BallModel{
    private final int hp;
    private int nowHp;

    public ObstacleBallModel(int id, int hp)
    {
        super(id);
        this.hp = hp;
        this.nowHp = hp;
    }

    public boolean isBroken()
    {
        return this.nowHp <= 0;
    }

    public void beHurt(int damage)
    {
        this.nowHp -= damage;
    }

}
