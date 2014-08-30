package com.aigirls.model;

public class ChoiceModel {
    final int leftX;
    final int lowY;
    final int width;
    final int height;

    public ChoiceModel(int x, int y, int w, int h)
    {
        leftX  = x;
        lowY   = y;
        width  = w;
        height = h;
    }

    public boolean isChoiced(int x, int y)
    {
        return leftX <= x && x < leftX+width && lowY <= y && y < lowY+height;
    }
}
