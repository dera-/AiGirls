package com.aigirls.io;

import com.aigirls.entity.GirlEntity;
import com.aigirls.io.file.GirlFileAccessor;

public class GirlEntityAccessor extends EntityAccessor{

    public GirlEntityAccessor()
    {
        super(new GirlFileAccessor());
    }

    public GirlEntity get(int id) {
        return (GirlEntity) super.get(id);
    }

}
