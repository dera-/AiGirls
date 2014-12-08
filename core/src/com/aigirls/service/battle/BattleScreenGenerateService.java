package com.aigirls.service.battle;

import com.aigirls.factory.BattleScreenFactory;
import com.aigirls.param.battle.EnemyType;
import com.aigirls.screen.battle.BattleScreen;

public class BattleScreenGenerateService extends Thread
{
    private int enemyId;
    private EnemyType enemyType;
    private BattleScreen battleScreen = null;

    public BattleScreenGenerateService(int enemyId, EnemyType type)
    {
        this.enemyId = enemyId;
        this.enemyType = type;
        start();
    }

    public void run()
    {
        battleScreen = BattleScreenFactory.generateBattleScreen(enemyId, enemyType);
    }

    public BattleScreen getBattleScreen()
    {
        return battleScreen;
    }

}
