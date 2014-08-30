package com.aigirls;

import com.aigirls.manager.ScreenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;

import java.util.Set;
import java.util.HashSet;

public class AiGirls extends Game {
    Screen nowScreen = null;

    @Override
    public void create () {

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
