package com.aigirls.io;

import com.aigirls.config.GameConfig;
import com.aigirls.entity.BaseEntity;
import com.aigirls.io.file.CsvFileAccessor;

public abstract class EntityAccessor {
    protected CsvFileAccessor csvFileAccessor;

    public EntityAccessor(CsvFileAccessor csv)
    {
        csvFileAccessor = csv;
    }

    public BaseEntity get(int id)
    {
        if (GameConfig.USE_DATABASE) {
            //TODO データベースから値を取得する場合
            return null;
        } else {
            return csvFileAccessor.getRecordById(Integer.toString(id));
        }
    }
}
