package com.aigirls.io;

import com.aigirls.config.GameConfig;
import com.aigirls.entity.BaseEntity;
import com.aigirls.entity.GirlMagicEntity;
import com.aigirls.io.file.GirlMagicFileAccessor;


public class GirlMagicEntityAccessor extends EntityAccessor {

    public GirlMagicEntityAccessor(){
        super(new GirlMagicFileAccessor());
    }

    public GirlMagicEntity get(int id){
        return (GirlMagicEntity)super.get(id);
    }

    public GirlMagicEntity[] getByCharacterId(int characterId){
        if (GameConfig.USE_DATABASE) {
            return null;
        } else {
            BaseEntity[] entities = csvFileAccessor.getRecords("character_id", Integer.toString(characterId));
            GirlMagicEntity[] girlMagicEntities = new GirlMagicEntity[entities.length];
            for (int i=0; i<girlMagicEntities.length; i++) {
                girlMagicEntities[i] = (GirlMagicEntity)entities[i];
            }
            return girlMagicEntities;
        }
    }

}
