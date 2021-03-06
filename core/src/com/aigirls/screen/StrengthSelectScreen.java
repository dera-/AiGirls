package com.aigirls.screen;


import com.aigirls.manager.PlayerIdManager;
import com.aigirls.manager.ScreenManager;
import com.aigirls.model.ChoiceListModel;
import com.aigirls.param.ScreenEnum;
import com.aigirls.view.StrengthSelectScreenView;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class StrengthSelectScreen extends GameScreen {
    private static StrengthSelectScreen selectScreen = null;
    private static int[] startNums = {9, 5, 1};

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
            Vector2 touchedPlace = getTouchedPlace(Gdx.input.getX(), Gdx.input.getY());
            int selected = getGameView().getChoicedPlace((int)touchedPlace.x, (int)touchedPlace.y);
            if (selected != ChoiceListModel.NOT_CHOICED) {
                setPlayerId(selected);
                ScreenManager.setNumber(startNums[selected]);
                ScreenManager.changeScreen(ScreenEnum.LoadingBattle);
            }
        }
    }

    protected StrengthSelectScreenView getGameView()
    {
        return (StrengthSelectScreenView) view;
    }

    private void setPlayerId(int selected)
    {
        int id = 1;
        switch(selected){
            case StrengthSelectScreenView.STRENGTH_WEAK:
                id = 1;
                break;
            case StrengthSelectScreenView.STRENGTH_NORMAL:
                id = 2;
                break;
            case StrengthSelectScreenView.STRENGTH_STRONG:
                id = 3;
                break;
        }
        PlayerIdManager.setPlayerId(id);
    }

}
