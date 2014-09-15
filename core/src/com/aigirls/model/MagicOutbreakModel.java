package com.aigirls.model;

import java.awt.Point;

public class MagicOutbreakModel {
    private int conditionWidth;
    private int conditionHeight;
    private Point[] conditionBallPlaces;

    public MagicOutbreakModel(int w, int h, Point[] conditions)
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

    public Point[] getConditionBallPlaces()
    {
        return conditionBallPlaces;
    }

}
