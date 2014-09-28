package com.aigirls.service.battle;

public class DamageCalculateService
{
    public static int getDamageValue(int attack, int defense)
    {
        return (int) Math.round(Math.pow(attack, 2)/(attack+defense));
    }

}
