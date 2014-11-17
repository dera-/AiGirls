package com.aigirls.screen.battle;

import java.awt.Point;

import com.aigirls.manager.ScreenManager;
import com.aigirls.param.ScreenEnum;
import com.aigirls.screen.GameScreen;
import com.aigirls.view.battle.ActionSelectView;
import com.aigirls.view.battle.BattleScreenView;
import com.badlogic.gdx.Gdx;

public class ActionSelectScreen extends GameScreen{
    private static ActionSelectScreen screen;

    public ActionSelectScreen(BattleScreenView battleView)
    {
        super(new ActionSelectView(battleView));
    }

    public static ActionSelectScreen getActionSelectScreen()
    {
        return screen;
    }

    public static void setActionSelectScreen(BattleScreenView battleViewer)
    {
        screen = new ActionSelectScreen(battleViewer);
    }

    @Override
    public void show() {
        getGameView().filledAll();
    }

    @Override
    public void hide() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    protected void update(float delta) {
        if (Gdx.input.justTouched()) {
            Point touchedPlace = getTouchedPlace(Gdx.input.getX(), Gdx.input.getY());
            switch (getGameView().getChoicedPlace(touchedPlace.x, touchedPlace.y)) {
                case ActionSelectView.MAGIC_ATTACK:
                    ScreenManager.changeScreen(ScreenEnum.GameAtMagicSelect);
                    break;
                case ActionSelectView.TURN_END:
                    ScreenManager.changeScreen(ScreenEnum.GameAtFinishTurn);
                    break;
                case ActionSelectView.RESET:
                    ScreenManager.changeScreen(ScreenEnum.GameAtActionReset);
                    break;
                default:
                    break;
            }
        }
    }

    protected ActionSelectView getGameView()
    {
        return (ActionSelectView) view;
    }
}
