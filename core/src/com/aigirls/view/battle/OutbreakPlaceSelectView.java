package com.aigirls.view.battle;

import com.aigirls.config.FileConfig;
import com.aigirls.config.GameConfig;
import com.aigirls.manager.TextureManager;
import com.aigirls.model.ChoiceListModel;
import com.aigirls.model.ChoiceModel;
import com.aigirls.model.battle.ActiveMagicModel;
import com.aigirls.model.battle.BallInfoModel;
import com.aigirls.model.battle.ObstacleBallInfoModel;
import com.aigirls.param.battle.PlayerEnum;
import com.aigirls.screen.battle.BattleScreen;
import com.aigirls.view.ButtomView;
import com.aigirls.view.SelectView;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

public class OutbreakPlaceSelectView extends SelectView {
    private static final int LEFT_ARROW_X = (int) Math.round(0.01*GameConfig.GAME_WIDTH);
    private static final int RIGHT_ARROW_X = (int) Math.round(0.45*GameConfig.GAME_WIDTH);
    private static final int ARROW_Y = (int) Math.round(0.03*GameConfig.GAME_HEIGHT);
    private static final int ARROW_WIDTH = (int) Math.round(0.1*GameConfig.GAME_WIDTH);
    private static final int ARROW_HEIGHT = (int) Math.round(0.1*GameConfig.GAME_HEIGHT);
    private static final int DECIDE_BUTTOM_X = (int) Math.round(0.15*GameConfig.GAME_WIDTH);
    private static final int CANCEL_BUTTOM_X = (int) Math.round(0.3*GameConfig.GAME_WIDTH);
    private static final int BUTTOM_Y = (int) Math.round(0.03*GameConfig.GAME_HEIGHT);
    private static final int BUTTOM_WIDTH = (int) Math.round(0.12*GameConfig.GAME_WIDTH);
    private static final int BUTTOM_HEIGHT = (int) Math.round(0.1*GameConfig.GAME_HEIGHT);
    public static final int DECIDE_BUTTOM_INDEX = 0;
    public static final int CANCEL_BUTTOM_INDEX = 1;
    public static final int LEFT_ARROW_INDEX = 2;
    public static final int RIGHT_ARROW_INDEX = 3;

    private BattleScreenView battleScreenView;
    private Sprite rightAllowSprite;
    private Sprite leftAllowSprite;
    private ButtomView decideButtomView;
    private ButtomView cancelButtomView;

    private ActiveMagicModel activeMagicModel = null;
    private Array<ObstacleBallInfoModel[]> obstaclesList = new Array<ObstacleBallInfoModel[]>();
    private int currentSelected = 0;

    public OutbreakPlaceSelectView (BattleScreenView battleScreenView) {
        super(0, 0, GameConfig.GAME_WIDTH, GameConfig.GAME_HEIGHT, 4);
        this.battleScreenView = battleScreenView;
        rightAllowSprite = new Sprite(TextureManager.getTexture(FileConfig.RIGHT_ARROW_KEY));
        leftAllowSprite = new Sprite(TextureManager.getTexture(FileConfig.LEFT_ARROW_KEY));
        decideButtomView = new ButtomView(DECIDE_BUTTOM_X, BUTTOM_Y, BUTTOM_WIDTH, BUTTOM_HEIGHT, "決定");
        cancelButtomView = new ButtomView(CANCEL_BUTTOM_X, BUTTOM_Y, BUTTOM_WIDTH, BUTTOM_HEIGHT, "戻る");
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
        batch.end();
        decideButtomView.draw(batch, shapeRenderer);
        cancelButtomView.draw(batch, shapeRenderer);
    }

    public void setActiveMagicModel(ActiveMagicModel magic)
    {
        activeMagicModel = magic;
    }

    public void setObstaclesList(Array<ObstacleBallInfoModel[]> list)
    {
        obstaclesList = list;
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
                if(currentSelected < 0) currentSelected = activeMagicModel.getNumTargetBalls()-1;
                setFlagsToBalls();
                break;
            case RIGHT_ARROW_INDEX:
                currentSelected++;
                if(currentSelected >= activeMagicModel.getNumTargetBalls()) currentSelected = 0;
                setFlagsToBalls();
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

    public void setFlagsToBalls()
    {
        setFlagsToBalls(currentSelected);
    }

    private void setFlagsToBalls(int index)
    {
        BallInfoModel[] ballInfoModels = activeMagicModel.getBallInfoModels(index);
        ObstacleBallInfoModel[] obstacleBallInfoModels = obstaclesList.get(index);
        battleScreenView.initializeBall(PlayerEnum.Player1);
        battleScreenView.setTargetBalls(ballInfoModels, PlayerEnum.Player1);
        battleScreenView.setFlagToObstacle(obstacleBallInfoModels, PlayerEnum.Player1);
    }

    public void formatTemporaryParameters()
    {
        battleScreenView.initializeBall(PlayerEnum.Player1);
        activeMagicModel = null;
        obstaclesList = new Array<ObstacleBallInfoModel[]>();
        currentSelected = 0;
        setTemporaryDamage(0);
    }

    public void filledNothing () {
        this.battleScreenView.setFilledAllowFlag(BattleScreen.ALLY_INDEX, false);
        this.battleScreenView.setFilledAllowFlag(BattleScreen.ENEMY_INDEX, false);
    }

    public void setTemporaryDamage(int damage)
    {
        this.battleScreenView.setTemporaryDamage(damage, PlayerEnum.Player2);
    }

}
