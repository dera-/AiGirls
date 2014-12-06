package com.aigirls.view.battle;

import com.aigirls.config.FileConfig;
import com.aigirls.config.GameConfig;
import com.aigirls.manager.TextureManager;
import com.aigirls.model.ChoiceListModel;
import com.aigirls.model.ChoiceModel;
import com.aigirls.param.battle.PlayerEnum;
import com.aigirls.screen.battle.BattleScreen;
import com.aigirls.view.ButtomView;
import com.aigirls.view.CharacterView;
import com.aigirls.view.SelectView;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class BattleEndScreenView extends SelectView {
    private static final int TITLE_BUTTOM_X = (int) Math.round(0.15*GameConfig.GAME_WIDTH);
    private static final int BUTTOM_Y = (int) Math.round(0.02*GameConfig.GAME_HEIGHT);
    private static final int BUTTOM_WIDTH = (int) Math.round(0.15*GameConfig.GAME_WIDTH);
    private static final int BUTTOM_HEIGHT = (int) Math.round(0.1*GameConfig.GAME_HEIGHT);
    private static final int IMAGE_ALLY_X = (int) Math.round(0.1*GameConfig.GAME_WIDTH);
    private static final int IMAGE_ENEMY_X = (int) Math.round(0.6*GameConfig.GAME_WIDTH);
    private static final int IMAGE_Y = (int) Math.round(0.4*GameConfig.GAME_HEIGHT);
    private static final int IMAGE_WIDTH = (int) Math.round(0.3*GameConfig.GAME_WIDTH);
    private static final int IMAGE_HEIGHT = (int) Math.round(0.2*GameConfig.GAME_HEIGHT);
    private static final int SELECT_ITEM_NUMS = 1;
    public static final int INDEX_TITLE_BUTTOM = 0;

    private BattleScreenView view;
    private ButtomView titleButtomView;
    private Sprite allyResultSprite = null;
    private Sprite enemyResultSprite = null;

    static {
        TextureManager.generateTexture(FileConfig.WIN_IMAGE_PATH, FileConfig.WIN_KEY);
        TextureManager.generateTexture(FileConfig.LOSE_IMAGE_PATH, FileConfig.LOSE_KEY);
    }

    public BattleEndScreenView(BattleScreenView view)
    {
        super(0, 0, GameConfig.GAME_WIDTH, GameConfig.GAME_HEIGHT, 1);
        this.view = view;
        this.titleButtomView = new ButtomView(TITLE_BUTTOM_X, BUTTOM_Y, BUTTOM_WIDTH, BUTTOM_HEIGHT, "タイトルへ");
    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        view.draw(batch, shapeRenderer);
        if (allyResultSprite!=null && enemyResultSprite!=null) {
            titleButtomView.draw(batch, shapeRenderer);
        }
        batch.begin();
        if (allyResultSprite != null) {
            allyResultSprite.setPosition(IMAGE_ALLY_X, IMAGE_Y);
            allyResultSprite.setSize(IMAGE_WIDTH, IMAGE_HEIGHT);
            allyResultSprite.draw(batch);
        }
        if (enemyResultSprite != null) {
            enemyResultSprite.setPosition(IMAGE_ENEMY_X, IMAGE_Y);
            enemyResultSprite.setSize(IMAGE_WIDTH, IMAGE_HEIGHT);
            enemyResultSprite.draw(batch);
        }
        batch.end();
    }

    @Override
    protected ChoiceListModel getChoiceListModel() {
        ChoiceModel[] choices = new ChoiceModel[SELECT_ITEM_NUMS];
        choices[INDEX_TITLE_BUTTOM] = new ChoiceModel(TITLE_BUTTOM_X, BUTTOM_Y, BUTTOM_WIDTH, BUTTOM_HEIGHT);
        ChoiceListModel listModel = new ChoiceListModel(choices);
        return listModel;
    }

    public void animation(float delta)
    {
        view.ballsAnimationInBoard(delta, PlayerEnum.Player1);
        view.ballsAnimationInBoard(delta, PlayerEnum.Player2);
    }

    public void startExploding()
    {
        view.startExploding(PlayerEnum.Player1);
        view.startExploding(PlayerEnum.Player2);
    }

    public void displayResult(boolean allyAlive, boolean enemyAlive)
    {
        view.removeBalls(PlayerEnum.Player1);
        view.removeBalls(PlayerEnum.Player2);
        allyResultSprite = getResultSprite(allyAlive);
        enemyResultSprite = getResultSprite(enemyAlive);
        int allyExpression = allyAlive ? CharacterView.EXPRESSION_WIN : CharacterView.EXPRESSION_LOSE;
        int enemyExpression = enemyAlive ? CharacterView.EXPRESSION_WIN : CharacterView.EXPRESSION_LOSE;
        view.changeCharaExpression(allyExpression, PlayerEnum.Player1);
        view.changeCharaExpression(enemyExpression, PlayerEnum.Player2);
    }

    private Sprite getResultSprite(boolean alive)
    {
        if (alive) {
            return new Sprite(TextureManager.getTexture(FileConfig.WIN_KEY));
        } else {
            return new Sprite(TextureManager.getTexture(FileConfig.LOSE_KEY));
        }
    }

    public void noFillScreen()
    {
        view.setFilledAllowFlag(BattleScreen.ALLY_INDEX, false);
        view.setFilledAllowFlag(BattleScreen.ENEMY_INDEX, false);
    }

}
