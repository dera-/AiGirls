package com.aigirls.view.battle;

import com.aigirls.config.FileConfig;
import com.aigirls.config.GameConfig;
import com.aigirls.model.ChoiceModel;
import com.aigirls.model.battle.BallInfoModel;
import com.aigirls.model.battle.CharacterViewModel;
import com.aigirls.model.battle.ObstacleBallInfoModel;
import com.aigirls.param.battle.PlayerEnum;
import com.aigirls.view.BallStackView;
import com.aigirls.view.BallView;
import com.aigirls.view.BoardView;
import com.aigirls.view.CharacterView;
import com.aigirls.view.FilledView;
import com.aigirls.view.GameView;
import com.aigirls.view.HpBarView;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class BattleScreenView extends GameView
{
    private static final int ALLY_IMAGE        = 0;
    private static final int ENEMY_IMAGE       = 1;
    private static final int ALLY_BOARD        = 2;
    private static final int ENEMY_BOARD       = 3;
    private static final int ALLY_HP_BAR       = 4;
    private static final int ENEMY_HP_BAR      = 5;
    private static final int ALLY_BALL_STACK   = 6;
    private static final int ENEMY_BALL_STACK  = 7;
    private static final int ELEMENT_NUMS      = 8;

    private static final int ALLY_X  = (int) Math.round(0.05 * GameConfig.GAME_WIDTH);
    private static final int ENEMY_X = (int) Math.round(0.53 * GameConfig.GAME_WIDTH);
    private static final int ENEMY_IMAGE_X = (int) Math.round(0.58 * GameConfig.GAME_WIDTH);
    private static final int CHARA_Y = (int) Math.round(0.28 * GameConfig.GAME_HEIGHT);
    private static final int HP_BAR_Y  = (int) Math.round(0.15 * GameConfig.GAME_HEIGHT);
    private static final int BOARD_Y  = (int) Math.round(0.25 * GameConfig.GAME_HEIGHT);
    private static final int BOARD_WIDTH   = (int) Math.round(0.42 * GameConfig.GAME_WIDTH);
    private static final int BOARD_HEIGHT  = BOARD_WIDTH;
    private static final int HP_BAR_WIDTH   = BOARD_WIDTH;
    private static final int HP_BAR_HEIGHT  = (int) Math.round(0.07 * GameConfig.GAME_HEIGHT);
    private static final int BOARD_SIDE_WALL_WIDTH = (int) Math.round(0.02*BOARD_WIDTH);
    private static final int BOARD_BOTTOM_WALL_WIDTH = 2*BOARD_SIDE_WALL_WIDTH;
    private static final int CHARACTER_WIDTH = BOARD_WIDTH - 2*BOARD_SIDE_WALL_WIDTH;
    private static final int CHARACTER_HEIGHT = BOARD_HEIGHT - BOARD_BOTTOM_WALL_WIDTH;
    private static final int BALL_STACK_Y  = (int) Math.round(0.82 * GameConfig.GAME_HEIGHT);
    private static final int BALL_STACK_WIDTH  = (int) Math.round(1.0*BOARD_WIDTH*GameConfig.LIMIT_STACKED_BALL_NUM/GameConfig.BOARD_WIDTH);
    private static final int BALL_STACK_HEIGHT  = (int) Math.round(0.1 * GameConfig.GAME_HEIGHT);
    private static final int BALL_STACK_WALL_WIDTH = BOARD_SIDE_WALL_WIDTH;
    private static final int BALL_STACK_WALL_HEIGHT = BOARD_SIDE_WALL_WIDTH;


    private GameView[] views;
    private FilledView[] filledViews;
    private boolean[] filledAllows;

    public BattleScreenView(CharacterViewModel ally, CharacterViewModel enemy)
    {
        super();
        views = new GameView[ELEMENT_NUMS];
        views[ALLY_IMAGE]   = new CharacterView(ALLY_X, CHARA_Y, CHARACTER_WIDTH, CHARACTER_HEIGHT, ally);
        views[ENEMY_IMAGE]  = new CharacterView(ENEMY_IMAGE_X, CHARA_Y, CHARACTER_WIDTH, CHARACTER_HEIGHT, enemy);
        views[ALLY_BOARD]   = new BoardView(ALLY_X, BOARD_Y, BOARD_WIDTH, BOARD_HEIGHT, BOARD_SIDE_WALL_WIDTH, BOARD_BOTTOM_WALL_WIDTH);
        views[ENEMY_BOARD]  = new BoardView(ENEMY_X, BOARD_Y, BOARD_WIDTH, BOARD_HEIGHT, BOARD_SIDE_WALL_WIDTH, BOARD_BOTTOM_WALL_WIDTH);
        views[ALLY_HP_BAR]  = new HpBarView(ALLY_X, HP_BAR_Y, HP_BAR_WIDTH, HP_BAR_HEIGHT, ally.getMaxHp());
        views[ENEMY_HP_BAR] = new HpBarView(ENEMY_X, HP_BAR_Y, HP_BAR_WIDTH, HP_BAR_HEIGHT, enemy.getMaxHp());
        views[ALLY_BALL_STACK] = new BallStackView(ALLY_X, BALL_STACK_Y, BALL_STACK_WIDTH, BALL_STACK_HEIGHT, BALL_STACK_WALL_WIDTH, BALL_STACK_WALL_HEIGHT, getBallName(PlayerEnum.Player1));
        views[ENEMY_BALL_STACK] = new BallStackView(ENEMY_X, BALL_STACK_Y, BALL_STACK_WIDTH, BALL_STACK_HEIGHT, BALL_STACK_WALL_WIDTH, BALL_STACK_WALL_HEIGHT, getBallName(PlayerEnum.Player2));

        filledViews = new FilledView[2];
        filledViews[0] = new FilledView(0, 0, (int) Math.round(0.5 * GameConfig.GAME_WIDTH), GameConfig.GAME_HEIGHT, new Color(0,0,0,0.5f));
        filledViews[1] = new FilledView((int) Math.round(0.5 * GameConfig.GAME_WIDTH), 0, (int) Math.round(0.5 * GameConfig.GAME_WIDTH), GameConfig.GAME_HEIGHT, new Color(0,0,0,0.5f));

        filledAllows = new boolean[2];
        filledAllows[0] = false;
        filledAllows[1] = false;
    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer){
        for(GameView view : views){
            view.draw(batch, shapeRenderer);
        }
        for (int i=0; i<filledAllows.length; i++) {
            if (filledAllows[i]) {
                filledViews[i].draw(batch, shapeRenderer);
            }
        }
    }

    public int getChoicedPlace(int x, int y)
    {
        BoardView viewer = (BoardView)views[ALLY_BOARD];
        return viewer.getChoicedPlace(x, y);
    }

    public void addBall(int id, int x, int y, PlayerEnum player)
    {
        removeFromBallStack(player);
        addBall(id, x, y, getBoardView(player), getBallName(player));
    }

    public void addObstacle(int id, int x, int y, PlayerEnum player)
    {
        addBall(id, x, y, getBoardView(player), FileConfig.OBSTACLE_KEY);
    }

    private void addBall(int id, int x, int y, BoardView board, String name)
    {
        if(board == null || name == null) return;
        board.addBall(id, x, y, name);
    }

    public void removeBall(int id, PlayerEnum player)
    {
        BoardView board = getBoardView(player);
        if (board == null) return;
        board.removeBall(id);
    }

    public void moveHpBar(int damage, PlayerEnum player)
    {
        HpBarView hpBar = getHpBarView(player);
        hpBar.setRestHp(damage);
    }

    public void initializeBall(PlayerEnum player)
    {
        BoardView board = getBoardView(player);
        board.initializeTargetFlags();
    }

    public void setTargetBalls(BallInfoModel[] ballsInfo, PlayerEnum player)
    {
        BoardView board = getBoardView(player);
        for (BallInfoModel ball : ballsInfo) {
            board.setTargetFlag(ball.id);
        }
    }

    public void setFlagToObstacle(ObstacleBallInfoModel[] ballsInfo, PlayerEnum player)
    {
        BoardView board = getBoardView(player);
        for (ObstacleBallInfoModel ball : ballsInfo) {
            int flag = (ball.isBroken()) ? BallView.FLAG_MEAN_BIG_DAMAGE : BallView.FLAG_MEAN_SMALL_DAMAGE;
            board.setFlagToBall(ball.id, flag);
        }
    }

    public void removeBalls(BallInfoModel[] ballsInfo, PlayerEnum player)
    {
        BoardView board = getBoardView(player);
        if (board == null) return;
        for (BallInfoModel ball : ballsInfo) {
            board.removeBall(ball.id);
        }
    }

    public void dropBalls(BallInfoModel[] ballsInfo, PlayerEnum player)
    {
        BoardView board = getBoardView(player);
        if (board == null) return;
        for (BallInfoModel ball : ballsInfo) {
            board.dropBall(ball.id, ball.y, true);
        }
    }

    public void setTemporaryDamage(int damage, PlayerEnum player)
    {
        HpBarView hpBar = getHpBarView(player);
        hpBar.setTemporaryDamage(damage);
    }

    private CharacterView getCharacter(PlayerEnum player)
    {
        switch(player) {
            case Player1:
                return (CharacterView) views[ALLY_IMAGE];
            case Player2:
                return (CharacterView) views[ENEMY_IMAGE];
            default:
                return null;
        }
    }


    private BoardView getBoardView(PlayerEnum player)
    {
        switch(player) {
            case Player1:
                return (BoardView) views[ALLY_BOARD];
            case Player2:
                return (BoardView) views[ENEMY_BOARD];
            default:
                return null;
        }
    }

    private HpBarView getHpBarView(PlayerEnum player)
    {
        switch(player) {
            case Player1:
                return (HpBarView) views[ALLY_HP_BAR];
            case Player2:
                return (HpBarView) views[ENEMY_HP_BAR];
            default:
                return null;
        }
    }

    private BallStackView getBallStackView(PlayerEnum player)
    {
        switch(player) {
            case Player1:
                return (BallStackView) views[ALLY_BALL_STACK];
            case Player2:
                return (BallStackView) views[ENEMY_BALL_STACK];
            default:
                return null;
        }
    }

    private String getBallName(PlayerEnum player)
    {
        switch(player) {
            case Player1:
                return FileConfig.BALL1_KEY;
            case Player2:
                return FileConfig.BALL2_KEY;
            default:
                return null;
        }
    }

    public void setFilledAllowFlag(int index, boolean allow)
    {
        if (index<0 || filledAllows.length<=index) {
            return;
        }
        filledAllows[index] = allow;
    }

    public void filledDefenderView(int defenderIndex)
    {
        filledAllows[defenderIndex] = true;
        filledAllows[(defenderIndex+1)%2] = false;
    }

    public ChoiceModel getAllyBallStackChoiceModel()
    {
        BallStackView ballStack = (BallStackView)views[ALLY_BALL_STACK];
        return ballStack.getChoiceModel();
    }

    public void addToBallStack(PlayerEnum player)
    {
        BallStackView ballStack = getBallStackView(player);
        ballStack.addBall(getBallName(player));
    }

    public void removeFromBallStack(PlayerEnum player)
    {
        BallStackView ballStack = getBallStackView(player);
        ballStack.removeBall();
    }

    public void displayBallInStack(PlayerEnum player, boolean displayFlag)
    {
        BallStackView ballStack = getBallStackView(player);
        ballStack.displayBall(displayFlag);
    }

    public void fillBoardOneLine(int x, int y, PlayerEnum player)
    {
        BoardView board = getBoardView(player);
        board.fillOneLine(x, y);
    }

    public void fillBoard(int x, int y, PlayerEnum player)
    {
        BoardView board = getBoardView(player);
        board.fillBoard(x, y);
    }

    public void noFillBoard(PlayerEnum player)
    {
        BoardView board = getBoardView(player);
        board.noFillOneLine();
    }

    public void startBallsAnimationInBoard(BallInfoModel[] targetBalls, PlayerEnum player)
    {
        BoardView board = getBoardView(player);
        for (BallInfoModel ball : targetBalls) {
            board.startBallAnimation(ball.id);
        }
    }

    public void ballsAnimationInBoard(float time, PlayerEnum player)
    {
        BoardView board = getBoardView(player);
        board.allBallsAnimation(time);
    }

    public void startBallsAnimationInStack(int num, PlayerEnum player)
    {
        BallStackView ballStack = getBallStackView(player);
        ballStack.startBallsAnimation(num, getBallName(player));
    }

    public void ballsAnimationInStack(float time, PlayerEnum player)
    {
        BallStackView ballStack = getBallStackView(player);
        ballStack.ballsAnimation(time);
    }

    public void displayBallsnInStack(PlayerEnum player)
    {
        BallStackView ballStack = getBallStackView(player);
        ballStack.displayBalls();
    }

    public void startAttackAnimation(PlayerEnum player)
    {
        BoardView board = getBoardView(player);
        board.startAttackAnimation();
    }

    public void attackAnimation(float time, PlayerEnum player)
    {
        BoardView board = getBoardView(player);
        board.attackAnimate(time);
    }

    public void changeCharaExpression(int index, PlayerEnum player)
    {
        CharacterView chara = getCharacter(player);
        chara.changeExpression(index);
    }

    public void startExploding(PlayerEnum player)
    {
        BoardView board = getBoardView(player);
        board.startExploding();
    }

    public void removeBalls(PlayerEnum player)
    {
        BoardView board = getBoardView(player);
        board.removeBalls();
    }

}
