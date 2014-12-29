package com.aigirls.screen.battle;

import com.badlogic.gdx.math.Vector2;
import com.aigirls.manager.ScreenManager;
import com.aigirls.model.ChoiceListModel;
import com.aigirls.model.battle.ActiveMagicModel;
import com.aigirls.param.ScreenEnum;
import com.aigirls.screen.GameScreen;
import com.aigirls.view.battle.BattleScreenView;
import com.aigirls.view.battle.MagicSelectView;
import com.badlogic.gdx.Gdx;

public class MagicSelectScreen extends GameScreen {
    private static MagicSelectScreen screen;
    private ActiveMagicModel[] activeMagicModels;

    private MagicSelectScreen(BattleScreenView battleScreenView, ActiveMagicModel[] magics){
        super(new MagicSelectView(battleScreenView, magics));
        activeMagicModels = magics;
    }

    public static MagicSelectScreen getMagicSelectScreen()
    {
        return screen;
    }

    public static void setMagicSelectScreen(BattleScreenView battleScreenView, ActiveMagicModel[] magics)
    {
        screen = new MagicSelectScreen(battleScreenView, magics);
    }

    @Override
    public void show() {
        getGameView().filledEnemyView();
    }

    @Override
    public void hide() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    protected void update(float delta)
    {
        if (Gdx.input.isTouched()) {
            Vector2 touchedPlace = getTouchedPlace(Gdx.input.getX(), Gdx.input.getY());
            if (getGameView().isSelectedMagicCard()) {
                getGameView().moveMagicCard((int)touchedPlace.x, (int)touchedPlace.y);
            } else {
                touchedEvent((int)touchedPlace.x, (int)touchedPlace.y);
            }
        } else if (getGameView().isSelectedMagicCard()) {
            ActiveMagicModel selectedMagic = getGameView().getMagic();
            getGameView().releaseMagicCard();
            if (selectedMagic != null && selectedMagic.canOutbreak()) {
                OutbreakPlaceSelectScreen.getOutbreakPlaceSelectScreen().setActiveMagicModel(selectedMagic);
                ScreenManager.changeScreen(ScreenEnum.GameAtOutbreakSelect);
            }
        }
    }

    private void touchedEvent(int x, int y)
    {
        int selected = getGameView().getChoicedPlace(x, y);
        if (selected == ChoiceListModel.NOT_CHOICED) {
            return;
        } else if (selected >= activeMagicModels.length) {
            ScreenManager.changeScreen(ScreenEnum.GameAtActionReset);
        } else {
            getGameView().selectMagicCard(selected, x, y);
        }
    }

    protected MagicSelectView getGameView() {
        return (MagicSelectView) view;
    }

}
