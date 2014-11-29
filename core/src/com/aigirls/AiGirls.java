package com.aigirls;

import com.aigirls.manager.ScreenManager;
import com.aigirls.param.ScreenEnum;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class AiGirls extends Game {
    Screen nowScreen = null;

    @Override
    public void create () {
        ScreenManager.changeScreen(ScreenEnum.StartBattle);
    }

    @Override
    public void render() {
        super.render();
        if (nowScreen != ScreenManager.getNowScreen()) {
            System.out.println("change");
            nowScreen = ScreenManager.getNowScreen();
            super.setScreen(nowScreen);
        }
    }

}
