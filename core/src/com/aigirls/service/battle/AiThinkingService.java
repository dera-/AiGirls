package com.aigirls.service.battle;

import com.aigirls.model.battle.CharacterModel;
import com.aigirls.model.battle.EnemyActionModel;
import com.aigirls.model.battle.EnemyCharacterModel;

public class AiThinkingService extends Thread{
    private CharacterModel playerChara;
    private EnemyCharacterModel aiChara;
    private EnemyActionModel aiAction = null;
    private int totalBallCount;

    public AiThinkingService(CharacterModel player, EnemyCharacterModel ai, int ballCount)
    {
        super();
        playerChara = player;
        aiChara = ai;
        totalBallCount = ballCount;
        this.start();
    }

    public void run()
    {
        aiAction = aiChara.getAction(playerChara, totalBallCount);
    }

    public EnemyActionModel getAiAction()
    {
        return aiAction;
    }

}
