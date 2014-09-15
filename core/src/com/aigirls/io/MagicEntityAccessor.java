package com.aigirls.io;

import com.aigirls.entity.MagicEntity;
import com.aigirls.io.file.MagicFileAccessor;

public class MagicEntityAccessor extends EntityAccessor {

    public MagicEntityAccessor() {
        super(new MagicFileAccessor());
    }

    public MagicEntity get(int id) {
        return (MagicEntity) super.get(id);
    }

}
