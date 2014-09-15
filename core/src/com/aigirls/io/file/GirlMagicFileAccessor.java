package com.aigirls.io.file;

import com.aigirls.config.FileConfig;
import com.aigirls.entity.BaseEntity;
import com.aigirls.entity.GirlMagicEntity;

public class GirlMagicFileAccessor extends CsvFileAccessor {

    public GirlMagicFileAccessor() {
        super(FileConfig.GIRL_MAGIC_FILE_PATH);
    }

    @Override
    protected BaseEntity getBaseEntity(String[] record) {
        GirlMagicEntity entity = new GirlMagicEntity();
        entity.id = Integer.parseInt(record[0]);
        entity.characterId = Integer.parseInt(record[1]);
        entity.magicId = Integer.parseInt(record[2]);
        return entity;
    }

}
