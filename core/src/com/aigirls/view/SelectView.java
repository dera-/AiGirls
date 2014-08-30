package com.aigirls.view;

import com.aigirls.model.ChoiceListModel;

public abstract class SelectView extends GameView {
    protected ChoiceListModel choiceList;

    public SelectView(int x, int y, int w, int h) {
        super(x, y, w, h);
        choiceList = getChoiceListModel();
    }

    protected abstract ChoiceListModel getChoiceListModel();

    public int getChoicedPlace(int x, int y)
    {
        return choiceList.getChoicedPlace(x, y);
    }

}
