package com.aigirls.screen.battle;

import java.awt.Point;

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

    public MagicSelectScreen(BattleScreenView battleScreenView, ActiveMagicModel[] magics){
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
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void update(float delta) {
        if (Gdx.input.justTouched()) {
            Point touchedPlace = getTouchedPlace(Gdx.input.getX(), Gdx.input.getY());
            int selected = getGameView().getChoicedPlace(touchedPlace.x, touchedPlace.y);
            if (selected == ChoiceListModel.NOT_CHOICED) {
                return;
            } else if (selected >= activeMagicModels.length) {
                ScreenManager.changeScreen(ScreenEnum.GameAtActionReset);
            } else if(activeMagicModels[selected].canOutbreak()) {
                OutbreakPlaceSelectScreen.getOutbreakPlaceSelectScreen().setActiveMagicModel(activeMagicModels[selected]);
                ScreenManager.changeScreen(ScreenEnum.GameAtOutbreakSelect);
            }
        }
    }

    protected MagicSelectView getGameView() {
        return (MagicSelectView) view;
    }

}
