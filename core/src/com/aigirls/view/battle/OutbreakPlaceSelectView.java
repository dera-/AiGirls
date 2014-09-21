package com.aigirls.view.battle;

import com.aigirls.config.FileConfig;
import com.aigirls.config.GameConfig;
import com.aigirls.manager.TextureManager;
import com.aigirls.model.ChoiceListModel;
import com.aigirls.model.ChoiceModel;
import com.aigirls.model.battle.ActiveMagicModel;
import com.aigirls.model.battle.BallInfoModel;
import com.aigirls.param.battle.PlayerEnum;
import com.aigirls.view.SelectView;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class OutbreakPlaceSelectView extends SelectView {
    private static final int LEFT_ARROW_X = (int) Math.round(0.05*GameConfig.GAME_WIDTH);
    private static final int RIGHT_ARROW_X = (int) Math.round(0.85*GameConfig.GAME_WIDTH);
    private static final int ARROW_Y = (int) Math.round(0.5*GameConfig.GAME_HEIGHT);
    private static final int ARROW_WIDTH = (int) Math.round(0.1*GameConfig.GAME_WIDTH);
    private static final int ARROW_HEIGHT = (int) Math.round(0.1*GameConfig.GAME_HEIGHT);
    private static final int DECIDE_BUTTOM_X = (int) Math.round(0.1*GameConfig.GAME_WIDTH);
    private static final int CANCEL_BUTTOM_X = (int) Math.round(0.3*GameConfig.GAME_WIDTH);
    private static final int BUTTOM_Y = (int) Math.round(0.95*GameConfig.GAME_HEIGHT);
    private static final int BUTTOM_WIDTH = (int) Math.round(0.1*GameConfig.GAME_WIDTH);
    private static final int BUTTOM_HEIGHT = (int) Math.round(0.1*GameConfig.GAME_HEIGHT);
    private static final int DECIDE_BUTTOM_INDEX = 0;
    private static final int CANCEL_BUTTOM_INDEX = 1;
    private static final int LEFT_ARROW_INDEX = 2;
    private static final int RIGHT_ARROW_INDEX = 3;

    private BattleScreenView battleScreenView;
    private Sprite rightAllowSprite;
    private Sprite leftAllowSprite;
    private Sprite decideButtomSprite;
    private Sprite cancelButtomSprite;

    private ActiveMagicModel activeMagicModel = null;
    private int numTargets = 0;
    private int currentSelected = 0;

    public OutbreakPlaceSelectView (BattleScreenView battleScreenView) {
        super(0, 0, GameConfig.GAME_WIDTH, GameConfig.GAME_HEIGHT, 4);
        this.battleScreenView = battleScreenView;
        rightAllowSprite = new Sprite(TextureManager.getTexture(FileConfig.RIGHT_ARROW_KEY));
        leftAllowSprite = new Sprite(TextureManager.getTexture(FileConfig.LEFT_ARROW_KEY));
        decideButtomSprite = new Sprite(TextureManager.getTexture(FileConfig.BUTTOM_KEY));
        cancelButtomSprite = new Sprite(TextureManager.getTexture(FileConfig.BUTTOM_KEY));
    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        battleScreenView.draw(batch, shapeRenderer);
        batch.begin();
        rightAllowSprite.setPosition(RIGHT_ARROW_X, ARROW_Y);
        rightAllowSprite.setSize(ARROW_WIDTH, ARROW_HEIGHT);
        rightAllowSprite.draw(batch);
        leftAllowSprite.setPosition(LEFT_ARROW_X, ARROW_Y);
        leftAllowSprite.setSize(ARROW_WIDTH, ARROW_HEIGHT);
        leftAllowSprite.draw(batch);
        decideButtomSprite.setPosition(DECIDE_BUTTOM_X, BUTTOM_Y);
        decideButtomSprite.setSize(BUTTOM_WIDTH, BUTTOM_HEIGHT);
        decideButtomSprite.draw(batch);
        cancelButtomSprite.setPosition(CANCEL_BUTTOM_X, BUTTOM_Y);
        cancelButtomSprite.setSize(BUTTOM_WIDTH, BUTTOM_HEIGHT);
        cancelButtomSprite.draw(batch);
        batch.end();
    }

    public void setActiveMagicModel(ActiveMagicModel magic)
    {
        activeMagicModel = magic;
        numTargets = activeMagicModel.getNumTargetBalls();
    }

    @Override
    protected ChoiceListModel getChoiceListModel() {
        ChoiceModel[] choices = new ChoiceModel[4];
        choices[DECIDE_BUTTOM_INDEX] = new ChoiceModel(DECIDE_BUTTOM_X, BUTTOM_Y, BUTTOM_WIDTH, BUTTOM_HEIGHT);
        choices[CANCEL_BUTTOM_INDEX] = new ChoiceModel(CANCEL_BUTTOM_X, BUTTOM_Y, BUTTOM_WIDTH, BUTTOM_HEIGHT);
        choices[LEFT_ARROW_INDEX] = new ChoiceModel(LEFT_ARROW_X, ARROW_Y, ARROW_WIDTH, ARROW_HEIGHT);
        choices[RIGHT_ARROW_INDEX] = new ChoiceModel(RIGHT_ARROW_X, ARROW_Y, ARROW_WIDTH, ARROW_HEIGHT);
        return new ChoiceListModel(choices);
    }

    public int getChoicedPlace(int x, int y)
    {
        int choiced = choiceList.getChoicedPlace(x, y);
        switch (choiced) {
            case LEFT_ARROW_INDEX:
                currentSelected--;
                if(currentSelected < 0) currentSelected = numTargets-1;
                setTargetBalls();
                break;
            case RIGHT_ARROW_INDEX:
                currentSelected++;
                if(currentSelected >= numTargets) currentSelected = 0;
                setTargetBalls();
                break;
        }
        return choiced;
    }

    public ActiveMagicModel getActiveMagicModel()
    {
        return activeMagicModel;
    }

    public BallInfoModel[] getBallInfoModels()
    {
        return activeMagicModel.getBallInfoModels(currentSelected);
    }

    public void setTargetBalls()
    {
        battleScreenView.setTargetBalls(activeMagicModel.getBallInfoModels(currentSelected), PlayerEnum.Player1);
        battleScreenView.setTargetBalls(activeMagicModel.getBallInfoModels(currentSelected), PlayerEnum.Player2);
    }

    public void formatTemporaryParameters()
    {
        battleScreenView.setTargetBalls(new BallInfoModel[0], PlayerEnum.Player1);
        battleScreenView.setTargetBalls(new BallInfoModel[0], PlayerEnum.Player2);
        activeMagicModel = null;
        numTargets = 0;
        currentSelected = 0;
    }

}
