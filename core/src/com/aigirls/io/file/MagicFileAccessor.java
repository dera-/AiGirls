package com.aigirls.io.file;

import com.aigirls.config.FileConfig;
import com.aigirls.entity.BaseEntity;
import com.aigirls.entity.MagicEntity;

public class MagicFileAccessor extends CsvFileAccessor {

    public MagicFileAccessor() {
        super(FileConfig.MAGIC_FILE_PATH);
    }

    @Override
    protected BaseEntity getBaseEntity(String[] record) {
        MagicEntity entity = new MagicEntity();
        entity.id = Integer.parseInt(record[0]);
        entity.name = record[1];
        entity.cost = Integer.parseInt(record[2]);
        entity.attackRate = Double.parseDouble(record[3]);
        entity.magicAttackRate = Double.parseDouble(record[4]);
        entity.outbreakFilePath = record[5];
        return entity;
    }

}
