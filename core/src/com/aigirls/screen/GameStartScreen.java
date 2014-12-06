package com.aigirls.screen;

import com.aigirls.manager.ScreenManager;
import com.aigirls.param.ScreenEnum;
import com.aigirls.view.GameStartScreenView;
import com.badlogic.gdx.Gdx;

public class GameStartScreen extends GameScreen {
    private static GameStartScreen startScreen = null;

    public static GameStartScreen getGameStartScreen()
    {
        if (startScreen == null) {
            startScreen = new GameStartScreen();
        }
        return startScreen;
    }

    private GameStartScreen() {
        super(new GameStartScreenView());
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
        if (Gdx.input.justTouched()) {
            ScreenManager.changeScreen(ScreenEnum.StrengthSelect);
        }
    }

}
