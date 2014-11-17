package com.aigirls.screen.battle;

import java.awt.Point;

import com.aigirls.manager.ScreenManager;
import com.aigirls.model.battle.ActiveMagicModel;
import com.aigirls.param.ScreenEnum;
import com.aigirls.screen.GameScreen;
import com.aigirls.view.battle.BattleScreenView;
import com.aigirls.view.battle.OutbreakPlaceSelectView;
import com.badlogic.gdx.Gdx;

public class OutbreakPlaceSelectScreen extends GameScreen {
    private static OutbreakPlaceSelectScreen screen;

    public OutbreakPlaceSelectScreen(BattleScreenView battleScreenView) {
        super(new OutbreakPlaceSelectView(battleScreenView));
    }

    public static OutbreakPlaceSelectScreen getOutbreakPlaceSelectScreen()
    {
        return screen;
    }

    public static void setOutbreakPlaceSelectScreen(BattleScreenView battleScreenView)
    {
        screen = new OutbreakPlaceSelectScreen(battleScreenView);
    }

    public void setActiveMagicModel(ActiveMagicModel magic)
    {
        getGameView().setActiveMagicModel(magic);
    }

    @Override
    public void show() {
        getGameView().filledNothing();
        getGameView().setTargetBalls();
    }

    @Override
    public void hide() {
        getGameView().formatTemporaryParameters();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    protected void update(float delta) {
        if (Gdx.input.justTouched()) {
            Point touchedPlace = getTouchedPlace(Gdx.input.getX(), Gdx.input.getY());
            int selected = getGameView().getChoicedPlace(touchedPlace.x, touchedPlace.y);
            if (selected == OutbreakPlaceSelectView.DECIDE_BUTTOM_INDEX) {
                MagicOutbreakScreen.getMagicOutbreakScreen().setOutbrokenMagic(
                    BattleScreen.ALLY_INDEX,
                    getGameView().getActiveMagicModel(),
                    getGameView().getBallInfoModels());
                ScreenManager.changeScreen(ScreenEnum.GameAtMagicOutbreak);
            } else if (selected == OutbreakPlaceSelectView.CANCEL_BUTTOM_INDEX) {
                ScreenManager.changeScreen(ScreenEnum.GameAtActionReset);
            }
        }
    }

    protected OutbreakPlaceSelectView getGameView()
    {
        return (OutbreakPlaceSelectView) view;
    }

}
