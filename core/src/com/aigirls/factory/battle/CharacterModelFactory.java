package com.aigirls.factory.battle;

import com.aigirls.entity.GirlMagicEntity;
import com.aigirls.entity.MonsterMagicEntity;
import com.aigirls.io.GirlEntityAccessor;
import com.aigirls.io.GirlMagicEntityAccessor;
import com.aigirls.io.MonsterEntityAccessor;
import com.aigirls.io.MonsterMagicEntityAccessor;
import com.aigirls.model.battle.CharacterModel;
import com.aigirls.model.battle.EnemyCharacterModel;
import com.aigirls.model.battle.MagicModel;
import com.aigirls.param.battle.EnemyType;

public class CharacterModelFactory {
    private static GirlEntityAccessor girlEntityAccessor;
    private static MonsterEntityAccessor monsterEntityAccessor;
    private static GirlMagicEntityAccessor girlMagicEntityAccessor;
    private static MonsterMagicEntityAccessor monsterMagicEntityAccessor;

    static{
        girlEntityAccessor = new GirlEntityAccessor();
        monsterEntityAccessor = new MonsterEntityAccessor();
        girlMagicEntityAccessor = new GirlMagicEntityAccessor();
        monsterMagicEntityAccessor = new MonsterMagicEntityAccessor();
    }

    public static CharacterModel generateCharacterModel(int id)
    {
        return new CharacterModel(girlEntityAccessor.get(id), getMagicsByGirlId(id));
    }

    public static EnemyCharacterModel generateEnemyCharacterModel(int id, EnemyType type)
    {
        switch (type) {
            case PLAYER:
                return new EnemyCharacterModel(girlEntityAccessor.get(id), getMagicsByGirlId(id));
            case MONSTER:
            default:
                return new EnemyCharacterModel(monsterEntityAccessor.get(id), getMagicsByMonsterId(id));
        }
    }

    private static MagicModel[] getMagicsByGirlId(int id)
    {
        GirlMagicEntity[] girlMagicEntity = girlMagicEntityAccessor.getByCharacterId(id);
        MagicModel[] magics = new MagicModel[girlMagicEntity.length];
        for (int i=0; i<magics.length; i++) {
            magics[i] = MagicModelFactory.generateMagicModel(girlMagicEntity[i].magicId);
        }
        return magics;
    }

    private static MagicModel[] getMagicsByMonsterId(int id)
    {
        MonsterMagicEntity[] monsterMagicEntity = monsterMagicEntityAccessor.getByCharacterId(id);
        MagicModel[] magics = new MagicModel[monsterMagicEntity.length];
        for (int i=0; i<magics.length; i++) {
            magics[i] = MagicModelFactory.generateMagicModel(monsterMagicEntity[i].magicId);
        }
        return magics;
    }

}
