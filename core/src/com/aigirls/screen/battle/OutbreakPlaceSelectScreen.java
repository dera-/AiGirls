package com.aigirls.screen.battle;

import com.aigirls.model.battle.ActiveMagicModel;
import com.aigirls.screen.GameScreen;
import com.aigirls.view.battle.BattleScreenView;
import com.aigirls.view.battle.OutbreakPlaceSelectView;

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
        //TODO ボタンを押したときの処理
    }

    protected OutbreakPlaceSelectView getGameView()
    {
        return (OutbreakPlaceSelectView) view;
    }

}
