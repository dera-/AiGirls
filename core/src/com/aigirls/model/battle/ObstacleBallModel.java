package com.aigirls.model.battle;

public class ObstacleBallModel extends BallModel{
    private final int hp;
    private int currentHp;

    public ObstacleBallModel(int id, int hp)
    {
        super(id);
        this.hp = hp;
        this.currentHp = hp;
    }

    public ObstacleBallModel getClone()
    {
        ObstacleBallModel model = new ObstacleBallModel(id, hp);
        model.currentHp = currentHp;
        return model;
    }

    public boolean isBroken()
    {
        return this.currentHp <= 0;
    }

    public void beHurt(int damage)
    {
        this.currentHp -= damage;
    }

    public int getCurrentHp()
    {
        return currentHp;
    }

}
