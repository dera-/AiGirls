package com.aigirls.io;

import com.aigirls.config.GameConfig;
import com.aigirls.entity.BaseEntity;
import com.aigirls.entity.MonsterMagicEntity;
import com.aigirls.io.file.MonsterMagicFileAccessor;

public class MonsterMagicEntityAccessor extends EntityAccessor {
    public MonsterMagicEntityAccessor(){
        super(new MonsterMagicFileAccessor());
    }

    public MonsterMagicEntity get(int id){
        return (MonsterMagicEntity)super.get(id);
    }

    public MonsterMagicEntity[] getByCharacterId(int characterId){
        if (GameConfig.USE_DATABASE) {
            return null;
        } else {
            BaseEntity[] entities = csvFileAccessor.getRecords("character_id", Integer.toString(characterId));
            MonsterMagicEntity[] monsterMagicEntities = new MonsterMagicEntity[entities.length];
            for (int i=0; i<monsterMagicEntities.length; i++) {
                monsterMagicEntities[i] = (MonsterMagicEntity)entities[i];
            }
            return monsterMagicEntities;
        }
    }

}
