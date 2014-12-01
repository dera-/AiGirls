package com.aigirls.model.battle;

public class ObstacleBallInfoModel extends BallInfoModel {
    private int currentHp;
    private int damage;

    public ObstacleBallInfoModel(int id, int x, int y, int hp, int damage) {
        super(id, x, y);
        this.currentHp = hp;
        this.damage = damage;
    }

    public boolean isSamePlace (int x, int y) {
        return (this.x == x && this.y == y);
    }

    public void addDamage(int d) {
        damage += d;
    }

    public boolean isBroken() {
        return (currentHp - damage) <= 0;
    }

    public int getDamage() {
        return damage;
    }

}
