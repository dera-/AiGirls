package com.aigirls.service.battle;

public class DamageCalculateService
{
    public static int getDamageValue(int attack, int defense)
    {
        return attack - defense;
    }

}
