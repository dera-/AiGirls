package com.aigirls.io.file;

import com.aigirls.config.FileConfig;
import com.aigirls.entity.BaseEntity;
import com.aigirls.entity.GirlEntity;

public class GirlFileAccessor extends CsvFileAccessor {


    public GirlFileAccessor() {
        super(FileConfig.GIRL_FILE_PATH);
    }

    @Override
    protected BaseEntity getBaseEntity(String[] record) {
        GirlEntity entity = new GirlEntity();
        entity.id = Integer.parseInt(record[0]);
        entity.name = record[1];
        entity.hp = Integer.parseInt(record[2]);
        entity.attack = Integer.parseInt(record[3]);
        entity.defense = Integer.parseInt(record[4]);
        entity.magicAttack = Integer.parseInt(record[5]);
        entity.magicDefense = Integer.parseInt(record[6]);
        entity.imageName = record[7];
        entity.lv = Integer.parseInt(record[8]);
        entity.loveValue = Integer.parseInt(record[9]);
        entity.charaType = Integer.parseInt(record[10]);
        entity.magicLimit = Integer.parseInt(record[11]);
        entity.reinforcementLimit = Integer.parseInt(record[12]);
        return entity;
    }

}
