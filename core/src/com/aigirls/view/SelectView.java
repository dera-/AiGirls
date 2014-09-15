package com.aigirls.view;

import com.aigirls.model.ChoiceListModel;

public abstract class SelectView extends GameView {
    protected ChoiceListModel choiceList;
    protected final int choiceItemNums;

    public SelectView(int x, int y, int w, int h, int nums) {
        super(x, y, w, h);
        choiceItemNums = nums;
        choiceList = getChoiceListModel();
    }

    protected abstract ChoiceListModel getChoiceListModel();

    public int getChoicedPlace(int x, int y)
    {
        return choiceList.getChoicedPlace(x, y);
    }

}
