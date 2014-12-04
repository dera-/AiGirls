package com.aigirls.view.battle;

import com.aigirls.config.GameConfig;

public interface TurnStartView {
    public static final String ALLY_TURN = "あなたのターン";
    public static final String ENEMY_TURN = "あいてのターン";
    public static final int X = (int) Math.round(0.1*GameConfig.GAME_WIDTH);
    public static final int Y = (int) Math.round(0.6*GameConfig.GAME_HEIGHT);

    public void display();
    public void dispose();
    public void animation(float time);
}
