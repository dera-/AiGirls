package com.aigirls.factory.battle;

import com.aigirls.io.MagicEntityAccessor;
import com.aigirls.model.battle.MagicModel;

public class MagicModelFactory {
    private static MagicEntityAccessor magicEntityAccessor;

    static {
        magicEntityAccessor = new MagicEntityAccessor();
    }

    public static MagicModel generateMagicModel(int id)
    {
        return new MagicModel(magicEntityAccessor.get(id));
    }

}
