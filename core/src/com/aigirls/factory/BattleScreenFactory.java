
package com.aigirls.factory;

import com.aigirls.factory.battle.CharacterModelFactory;
import com.aigirls.manager.PlayerIdManager;
import com.aigirls.model.battle.CharacterModel;
import com.aigirls.model.battle.EnemyCharacterModel;
import com.aigirls.param.battle.EnemyType;
import com.aigirls.screen.battle.BattleScreen;

public class BattleScreenFactory {

    public BattleScreen generateBattleScreen(int enemyId, EnemyType type)
    {
        CharacterModel ally = CharacterModelFactory.generateCharacterModel(PlayerIdManager.getPlayerId());
        EnemyCharacterModel enemy = CharacterModelFactory.generateEnemyCharacterModel(enemyId, type);
        return new BattleScreen(ally, enemy);
    }

}
