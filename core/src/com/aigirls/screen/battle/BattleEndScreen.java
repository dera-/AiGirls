package com.aigirls.screen.battle;

import com.badlogic.gdx.math.Vector2;
import com.aigirls.manager.ScreenManager;
import com.aigirls.model.battle.CharacterModel;
import com.aigirls.param.ScreenEnum;
import com.aigirls.screen.GameScreen;
import com.aigirls.view.battle.BattleEndScreenView;
import com.aigirls.view.battle.BattleScreenView;
import com.badlogic.gdx.Gdx;

public class BattleEndScreen extends GameScreen {
    private static BattleEndScreen screen;
    private static final float EXPLODE_TIME = 0.5f;
    private static final float DISPLAY_RESULT_TIME = 1.2f;

    private CharacterModel[] players;
    private float currentTime = 0;

    public static void setBattleEndScreen(BattleScreenView battleScreenView, CharacterModel[] players)
    {
        screen = new BattleEndScreen(battleScreenView, players);
    }

    public static BattleEndScreen getBattleEndScreen()
    {
        return screen;
    }

    private BattleEndScreen(BattleScreenView battleScreenView, CharacterModel[] players) {
        super(new BattleEndScreenView(battleScreenView));
        this.players = players;
    }

    @Override
    public void show() {
        currentTime = 0;
        getGameView().noFillScreen();
        getGameView().dicideUnusableButtom(players[BattleScreen.ALLY_INDEX].isAlive());
    }

    @Override
    public void hide() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    protected void update(float delta) {
        if (isBeyondTargetTime(EXPLODE_TIME, delta)) {
            getGameView().startExploding();
        } else if (isBeyondTargetTime(DISPLAY_RESULT_TIME, delta)) {
            getGameView().displayResult(players[BattleScreen.ALLY_INDEX].isAlive(), players[BattleScreen.ENEMY_INDEX].isAlive());
        }
        if (currentTime < DISPLAY_RESULT_TIME) {
            currentTime += delta;
            getGameView().animation(delta);
            return;
        }
        if (Gdx.input.justTouched()) {
            Vector2 touchedPlace = getTouchedPlace(Gdx.input.getX(), Gdx.input.getY());
            int selected = getGameView().getChoicedPlace((int)touchedPlace.x, (int)touchedPlace.y);
            if (selected == BattleEndScreenView.INDEX_TITLE_BUTTOM) {
                ScreenManager.changeScreen(ScreenEnum.GameAtReturnTitle);
            } else if (selected == BattleEndScreenView.INDEX_RETRY_BUTTOM && !players[BattleScreen.ALLY_INDEX].isAlive()) {
                ScreenManager.changeScreen(ScreenEnum.GameAtRetry);
            } else if (selected == BattleEndScreenView.INDEX_NEXT_BUTTOM && players[BattleScreen.ALLY_INDEX].isAlive()) {
                ScreenManager.changeScreen(ScreenEnum.GameAtNext);
            }
        }
    }

    private boolean isBeyondTargetTime(float targetTime, float delta)
    {
        return currentTime < targetTime && targetTime <= (currentTime+delta);
    }

    protected BattleEndScreenView getGameView()
    {
        return (BattleEndScreenView) view;
    }
}
