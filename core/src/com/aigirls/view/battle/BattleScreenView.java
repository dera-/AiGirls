package com.aigirls.view.battle;

import com.aigirls.config.GameConfig;
import com.aigirls.view.BoardView;
import com.aigirls.view.CharacterView;
import com.aigirls.view.GameView;
import com.aigirls.view.HpBarView;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class BattleScreenView extends GameView
{
    private GameView[] views;
    private static final int ALLY_IMAGE   = 0;
    private static final int ENEMY_IMAGE  = 1;
    private static final int ALLY_BOARD   = 2;
    private static final int ENEMY_BOARD  = 3;
    private static final int ALLY_HP_BAR  = 4;
    private static final int ENEMY_HP_BAR = 5;

    private static final int ALLY_X  = (int) Math.round(0.05 * GameConfig.GAME_WIDTH);
    private static final int ENEMY_X = (int) Math.round(0.53 * GameConfig.GAME_WIDTH);
    private static final int HP_BAR_Y  = (int) Math.round(0.1 * GameConfig.GAME_HEIGHT);
    private static final int BOARD_Y  = (int) Math.round(0.25 * GameConfig.GAME_HEIGHT);
    private static final int BOARD_WIDTH   = (int) Math.round(0.42 * GameConfig.GAME_WIDTH);
    private static final int BOARD_HEIGHT  = BOARD_WIDTH;
    private static final int HP_BAR_WIDTH   = BOARD_WIDTH;
    private static final int HP_BAR_HEIGHT  = (int) Math.round(0.1 * GameConfig.GAME_HEIGHT);
    private static final int BOARD_SIDE_WALL_WIDTH = (int) Math.round(0.05*BOARD_WIDTH);
    private static final int BOARD_BOTTOM_WALL_WIDTH = 2*BOARD_SIDE_WALL_WIDTH;
    private static final int CHARACTER_WIDTH = BOARD_WIDTH - 2*BOARD_SIDE_WALL_WIDTH;
    private static final int CHARACTER_HEIGHT = BOARD_HEIGHT - BOARD_BOTTOM_WALL_WIDTH;


    public BattleScreenView(String allyImageName, String enemyImageName)
    {
        super();
        views = new GameView[6];
        views[ALLY_IMAGE]   = new CharacterView(ALLY_X, BOARD_Y, CHARACTER_WIDTH, CHARACTER_HEIGHT, allyImageName);
        views[ENEMY_IMAGE]  = new CharacterView(ENEMY_X, BOARD_Y, CHARACTER_WIDTH, CHARACTER_HEIGHT, enemyImageName);
        views[ALLY_BOARD]   = new BoardView(ALLY_X, BOARD_Y, BOARD_WIDTH, BOARD_HEIGHT, BOARD_SIDE_WALL_WIDTH, BOARD_BOTTOM_WALL_WIDTH);
        views[ENEMY_BOARD]  = new BoardView(ENEMY_X, BOARD_Y, BOARD_WIDTH, BOARD_HEIGHT, BOARD_SIDE_WALL_WIDTH, BOARD_BOTTOM_WALL_WIDTH);
        views[ALLY_HP_BAR]  = new HpBarView(ALLY_X, HP_BAR_Y, HP_BAR_WIDTH, HP_BAR_HEIGHT);
        views[ENEMY_HP_BAR] = new HpBarView(ENEMY_X, HP_BAR_Y, HP_BAR_WIDTH, HP_BAR_HEIGHT);
    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        for(GameView view : views){
            view.draw(batch, shapeRenderer);
        }
    }

    public int getChoicedPlace(int x, int y)
    {
        BoardView viewer = (BoardView)views[ALLY_BOARD];
        return viewer.getChoicedPlace(x, y);
    }

}
