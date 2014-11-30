package com.aigirls.view.battle;

import com.aigirls.config.FileConfig;
import com.aigirls.config.GameConfig;
import com.aigirls.model.ChoiceListModel;
import com.aigirls.model.ChoiceModel;
import com.aigirls.param.battle.PlayerEnum;
import com.aigirls.view.BallView;
import com.aigirls.view.ButtomView;
import com.aigirls.view.SelectView;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class PlayerTurnScreenView extends SelectView {
    private static final int DECIDE_BUTTOM_X = (int) Math.round(0.05*GameConfig.GAME_WIDTH);
    private static final int CANCEL_BUTTOM_X = (int) Math.round(0.15*GameConfig.GAME_WIDTH);
    private static final int FINISH_BUTTOM_X = (int) Math.round(0.25*GameConfig.GAME_WIDTH);
    private static final int BUTTOM_Y = (int) Math.round(0.01*GameConfig.GAME_HEIGHT);
    private static final int BUTTOM_WIDTH = (int) Math.round(0.1*GameConfig.GAME_WIDTH);
    private static final int BUTTOM_HEIGHT = (int) Math.round(0.1*GameConfig.GAME_HEIGHT);
    private static final int SELECT_ITEM_NUMS = 4;
    public static final int INDEX_BALL_STACK = 0;
    public static final int INDEX_DECIDE_BUTTOM = 1;
    public static final int INDEX_CANCEL_BUTTOM = 2;
    public static final int INDEX_FINISH_BUTTOM = 3;
    private static final int SELECTED_BALL_SIZE = 48;

    private BattleScreenView battleScreenView;
    //ボタン
    private ButtomView decideButtomView;
    private ButtomView cancelButtomView;
    private ButtomView finishButtomView;
    //ボール
    private BallView selectedBallView;

    public PlayerTurnScreenView(BattleScreenView battleScreenView){
        super(0, 0, GameConfig.GAME_WIDTH, GameConfig.GAME_HEIGHT, SELECT_ITEM_NUMS);
        this.battleScreenView = battleScreenView;
        decideButtomView = new ButtomView(DECIDE_BUTTOM_X, BUTTOM_Y, BUTTOM_WIDTH, BUTTOM_HEIGHT, "攻撃");
        cancelButtomView = new ButtomView(CANCEL_BUTTOM_X, BUTTOM_Y, BUTTOM_WIDTH, BUTTOM_HEIGHT, "戻る");
        finishButtomView = new ButtomView(FINISH_BUTTOM_X, BUTTOM_Y, BUTTOM_WIDTH, BUTTOM_HEIGHT, "終了", new Color(0.8f, 0, 0, 1), new Color(1, 1, 1, 1));
        cancelButtomView.setCanPush(false);
        choiceList = getChoiceListModel();
        selectedBallView = null;
    }

    public BattleScreenView getBattleScreenView() {
        return battleScreenView;
    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        battleScreenView.draw(batch, shapeRenderer);
        decideButtomView.draw(batch, shapeRenderer);
        cancelButtomView.draw(batch, shapeRenderer);
        finishButtomView.draw(batch, shapeRenderer);
        if (selectedBallView != null) {
            selectedBallView.draw(batch, shapeRenderer);
        }
    }

    @Override
    protected ChoiceListModel getChoiceListModel() {
        if (battleScreenView == null) {
            return null;
        }
        ChoiceModel[] choices = new ChoiceModel[SELECT_ITEM_NUMS];
        choices[INDEX_BALL_STACK] = battleScreenView.getAllyBallStackChoiceModel();
        choices[INDEX_DECIDE_BUTTOM] = new ChoiceModel(DECIDE_BUTTOM_X, BUTTOM_Y, BUTTOM_WIDTH, BUTTOM_HEIGHT);
        choices[INDEX_CANCEL_BUTTOM] = new ChoiceModel(CANCEL_BUTTOM_X, BUTTOM_Y, BUTTOM_WIDTH, BUTTOM_HEIGHT);
        choices[INDEX_FINISH_BUTTOM] = new ChoiceModel(FINISH_BUTTOM_X, BUTTOM_Y, BUTTOM_WIDTH, BUTTOM_HEIGHT);
        ChoiceListModel listModel = new ChoiceListModel(choices);
        return listModel;
    }

    public void setCancelButtomFlag(boolean flag) {
        cancelButtomView.setCanPush(flag);
    }

    public void pushOnBallStack(int x, int y)
    {
        selectedBallView = new BallView(0, x, y, SELECTED_BALL_SIZE, FileConfig.BALL1_KEY);
        battleScreenView.displayBallInStack(PlayerEnum.Player1, false);
    }

    public void releaseBall(int xPlace)
    {
        selectedBallView = null;
        battleScreenView.noFillBoard();
        if (xPlace == ChoiceListModel.NOT_CHOICED) {
            battleScreenView.displayBallInStack(PlayerEnum.Player1, true);
        }
    }

    public void moveBall(int x, int y)
    {
        selectedBallView.setPlace(x-SELECTED_BALL_SIZE/2, y-SELECTED_BALL_SIZE/2);
        battleScreenView.fillBoardOneLine(x, y);
    }

    public int getChoicedPlace(int x, int y)
    {
        int index = super.getChoicedPlace(x, y);
        if (index == INDEX_CANCEL_BUTTOM && !cancelButtomView.canPush()) {
            return ChoiceListModel.NOT_CHOICED;
        }
        return index;
    }

}
