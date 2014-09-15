package com.aigirls.screen.battle;

import java.awt.Point;

import com.aigirls.model.ChoiceListModel;
import com.aigirls.model.battle.ActiveMagicModel;
import com.aigirls.screen.GameScreen;
import com.aigirls.view.battle.ActionSelectView;
import com.aigirls.view.battle.MagicSelectView;
import com.badlogic.gdx.Gdx;

public class MagicSelectScreen extends GameScreen {
    private static MagicSelectScreen screen;
    private ActiveMagicModel[] activeMagicModels;

    public MagicSelectScreen(ActiveMagicModel[] magics){
        super(new MagicSelectView(magics));
        activeMagicModels = magics;
    }

    public static MagicSelectScreen getMagicSelectScreen()
    {
        return screen;
    }

    public static void setMagicSelectScreen(ActiveMagicModel[] magics)
    {
        screen = new MagicSelectScreen(magics);
    }

    @Override
    public void show() {
        // TODO Auto-generated method stub

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
        if (Gdx.input.isTouched()) {
            Point touchedPlace = getTouchedPlace(Gdx.input.getX(), Gdx.input.getY());
            int selected = getGameView().getChoicedPlace(touchedPlace.x, touchedPlace.y);
            if (selected == ChoiceListModel.NOT_CHOICED) {
                return;
            } else if (selected >= activeMagicModels.length) {
                
            } else {
                
            }
        }
    }

    protected MagicSelectView getGameView() {
        return (MagicSelectView) view;
    }

}
