package com.aigirls;

import com.aigirls.manager.ScreenManager;
import com.aigirls.param.ScreenEnum;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class AiGirls extends Game {
    Screen nowScreen = null;

    @Override
    public void create () {
        ScreenManager.changeScreen(ScreenEnum.StartGame);
    }

    @Override
    public void render() {
        super.render();
        if (nowScreen != ScreenManager.getNowScreen()) {
            nowScreen = ScreenManager.getNowScreen();
            super.setScreen(nowScreen);
        }
    }

}
