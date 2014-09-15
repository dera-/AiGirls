package com.aigirls.io.file;

import com.aigirls.config.FileConfig;
import com.aigirls.entity.BaseEntity;
import com.aigirls.entity.MonsterMagicEntity;

public class MonsterMagicFileAccessor extends CsvFileAccessor {

    public MonsterMagicFileAccessor() {
        super(FileConfig.MONSTER_MAGIC_FILE_PATH);
    }

    @Override
    protected BaseEntity getBaseEntity(String[] record) {
        MonsterMagicEntity entity = new MonsterMagicEntity();
        entity.id = Integer.parseInt(record[0]);
        entity.characterId = Integer.parseInt(record[1]);
        entity.magicId = Integer.parseInt(record[2]);
        return entity;
    }

}
