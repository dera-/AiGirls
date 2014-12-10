package com.aigirls.model;

import com.badlogic.gdx.math.Vector2;

public class MagicOutbreakModel {
    private int conditionWidth;
    private int conditionHeight;
    private Vector2[] conditionBallPlaces;

    public MagicOutbreakModel(int w, int h, Vector2[] conditions)
    {
        conditionWidth = w;
        conditionHeight = h;
        conditionBallPlaces = conditions;
    }

    public int getConditionWidth()
    {
        return conditionWidth;
    }

    public int getConditionHeight()
    {
        return conditionHeight;
    }

    public Vector2[] getConditionBallPlaces()
    {
        return conditionBallPlaces;
    }

}
