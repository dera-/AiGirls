package com.aigirls.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.aigirls.AiGirls;
import com.aigirls.config.GameConfig;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(GameConfig.GAME_WIDTH, GameConfig.GAME_HEIGHT);
        }

        @Override
        public ApplicationListener getApplicationListener () {
                return new AiGirls();
        }
}