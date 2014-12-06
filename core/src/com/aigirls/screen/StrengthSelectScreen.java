package com.aigirls.screen;

import java.awt.Point;

import com.aigirls.manager.ScreenManager;
import com.aigirls.model.ChoiceListModel;
import com.aigirls.param.ScreenEnum;
import com.aigirls.view.StrengthSelectScreenView;
import com.badlogic.gdx.Gdx;

public class StrengthSelectScreen extends GameScreen {
    private static StrengthSelectScreen selectScreen = null;
    private static int[] startNums = {17, 13, 9, 5, 1};

    public static StrengthSelectScreen getStrengthSelect()
    {
        if (selectScreen == null) {
            selectScreen = new StrengthSelectScreen();
        }
        return selectScreen;
    }

    public StrengthSelectScreen() {
        super(new StrengthSelectScreenView());
    }

    @Override
    public void show() {}

    @Override
    public void hide() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    protected void update(float delta) {
        getGameView().wavingAnimation(delta);
        if (Gdx.input.justTouched()) {
            Point touchedPlace = getTouchedPlace(Gdx.input.getX(), Gdx.input.getY());
            int selected = getGameView().getChoicedPlace(touchedPlace.x, touchedPlace.y);
            if (selected != ChoiceListModel.NOT_CHOICED) {
                ScreenManager.setNumber(startNums[selected]);
                ScreenManager.changeScreen(ScreenEnum.StartBattle);
            }
        }
    }

    protected StrengthSelectScreenView getGameView()
    {
        return (StrengthSelectScreenView) view;
    }

}
