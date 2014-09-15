package com.aigirls.io.file;

import com.aigirls.config.FileConfig;
import com.aigirls.entity.BaseEntity;
import com.aigirls.entity.MonsterEntity;

public class MonsterFileAccessor extends CsvFileAccessor {

    public MonsterFileAccessor() {
        super(FileConfig.MONSTER_FILE_PATH);
    }

    @Override
    protected BaseEntity getBaseEntity(String[] record) {
        MonsterEntity entity = new MonsterEntity();
        entity.id = Integer.parseInt(record[0]);
        entity.name = record[1];
        entity.hp = Integer.parseInt(record[2]);
        entity.attack = Integer.parseInt(record[3]);
        entity.defense = Integer.parseInt(record[4]);
        entity.magicAttack = Integer.parseInt(record[5]);
        entity.magicDefense = Integer.parseInt(record[6]);
        entity.imageName = record[7];
        return entity;
    }

}
