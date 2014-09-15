package com.aigirls.io;

import com.aigirls.entity.MonsterEntity;
import com.aigirls.io.file.MonsterFileAccessor;

public class MonsterEntityAccessor extends EntityAccessor {

    public MonsterEntityAccessor() {
        super(new MonsterFileAccessor());
    }

    public MonsterEntity get(int id) {
        return (MonsterEntity) super.get(id);
    }
}
