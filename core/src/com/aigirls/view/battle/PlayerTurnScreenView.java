package com.aigirls.view.battle;

import com.aigirls.config.FileConfig;
import com.aigirls.config.GameConfig;
import com.aigirls.manager.BitmapFontManager;
import com.aigirls.model.ChoiceListModel;
import com.aigirls.model.ChoiceModel;
import com.aigirls.model.battle.ActiveMagicModel;
import com.aigirls.model.battle.BoardModel;
import com.aigirls.param.battle.PlayerEnum;
import com.aigirls.screen.battle.BattleScreen;
import com.aigirls.view.BallView;
import com.aigirls.view.ButtomView;
import com.aigirls.view.MagicCardListView;
import com.aigirls.view.MagicCardView;
import com.aigirls.view.SelectView;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class PlayerTurnScreenView extends SelectView implements TurnStartView {
    private static final int DECIDE_BUTTOM_X = (int) Math.round(0.05*GameConfig.GAME_WIDTH);
    private static final int CANCEL_BUTTOM_X = (int) Math.round(0.20*GameConfig.GAME_WIDTH);
    private static final int FINISH_BUTTOM_X = (int) Math.round(0.35*GameConfig.GAME_WIDTH);
    private static final int ENEMY_SKILL_BUTTOM_X = (int) Math.round(0.65*GameConfig.GAME_WIDTH);
    private static final int BUTTOM_Y = (int) Math.round(0.01*GameConfig.GAME_HEIGHT);
    private static final int BUTTOM_WIDTH = (int) Math.round(0.1*GameConfig.GAME_WIDTH);
    private static final int ENEMY_SKILL_BUTTOM_WIDTH = (int) Math.round(0.15*GameConfig.GAME_WIDTH);
    private static final int BUTTOM_HEIGHT = (int) Math.round(0.1*GameConfig.GAME_HEIGHT);
    private static final int SELECT_ITEM_NUMS = 5;
    public static final int INDEX_BALL_STACK = 0;
    public static final int INDEX_DECIDE_BUTTOM = 1;
    public static final int INDEX_CANCEL_BUTTOM = 2;
    public static final int INDEX_FINISH_BUTTOM = 3;
    public static final int INDEX_ENEMY_SKILL_BUTTOM = 4;
    private static final int SELECTED_BALL_SIZE = 48;

    private static int ENEMY_MAGICS_START_X = (int) Math.round(0.5 * GameConfig.GAME_WIDTH);
    private static int ENEMY_MAGICS_START_Y = (int) Math.round(0.65 * GameConfig.GAME_HEIGHT);
    private static int ENEMY_MAGICS_HORAIZONTAL_INTERVAL = (int) Math.round(0.02 * GameConfig.GAME_WIDTH);
    private static int ENEMY_MAGICS_VERTICAL_INTERVAL = (int) Math.round(0.05 * GameConfig.GAME_HEIGHT);

    private BattleScreenView battleScreenView;

    private MagicCardListView cardListView = null;
    private MagicCardView selectedCardView = null;

    //ボタン
    private ButtomView decideButtomView;
    private ButtomView cancelButtomView;
    private ButtomView finishButtomView;
    private ButtomView enemySkillButtomView;
    //ボール
    private BallView selectedBallView;

    private BitmapFont font;
    private boolean turnStartFlag = false;

    public PlayerTurnScreenView(BattleScreenView battleScreenView){
        super(0, 0, GameConfig.GAME_WIDTH, GameConfig.GAME_HEIGHT, SELECT_ITEM_NUMS);
        this.battleScreenView = battleScreenView;
        decideButtomView = new ButtomView(DECIDE_BUTTOM_X, BUTTOM_Y, BUTTOM_WIDTH, BUTTOM_HEIGHT, "攻撃");
        cancelButtomView = new ButtomView(CANCEL_BUTTOM_X, BUTTOM_Y, BUTTOM_WIDTH, BUTTOM_HEIGHT, "戻す");
        finishButtomView = new ButtomView(FINISH_BUTTOM_X, BUTTOM_Y, BUTTOM_WIDTH, BUTTOM_HEIGHT, "終了", new Color(0.8f, 0, 0, 1), new Color(1, 1, 1, 1));
        enemySkillButtomView = new ButtomView(ENEMY_SKILL_BUTTOM_X, BUTTOM_Y, ENEMY_SKILL_BUTTOM_WIDTH, BUTTOM_HEIGHT, "敵情報", new Color(0.1f, 0.8f, 0.3f, 1), new Color(1, 1, 1, 1));
        cancelButtomView.setCanPush(false);
        choiceList = getChoiceListModel();
        font = BitmapFontManager.getBitmapFont(FileConfig.NYANKO_FONT_KEY);
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
        enemySkillButtomView.draw(batch, shapeRenderer);
        if (selectedBallView != null) {
            selectedBallView.draw(batch, shapeRenderer);
        }
        if (cardListView != null) {
            batch.begin();
            font.setColor(0.9f, 0.15f, 0.15f, 1f);
            font.draw(batch, "敵 技一覧", (int) Math.round(0.8 * GameConfig.GAME_WIDTH), (int) Math.round(0.98 * GameConfig.GAME_HEIGHT));
            batch.end();
            cardListView.draw(batch, shapeRenderer);
            if (selectedCardView != null) {
                batch.begin();
                font.setColor(0.8f, 0.3f, 0.3f, 1f);
                font.draw(batch, "技名："+selectedCardView.getMagicName(), ENEMY_MAGICS_START_X, (int) Math.round(0.4 * GameConfig.GAME_HEIGHT));
                font.draw(batch, "効果1：相手へ"+selectedCardView.getAttackPercent()+"%攻撃", ENEMY_MAGICS_START_X, (int) Math.round(0.32 * GameConfig.GAME_HEIGHT));
                font.draw(batch, "効果2:邪魔玉へ"+selectedCardView.getBallAttackPercent()+"%攻撃", ENEMY_MAGICS_START_X, (int) Math.round(0.24 * GameConfig.GAME_HEIGHT));
                font.draw(batch, "効果3:"+selectedCardView.getRecoverBall()+"個ボール補充", ENEMY_MAGICS_START_X, (int) Math.round(0.16 * GameConfig.GAME_HEIGHT));
                batch.end();
            }
        }
        if (turnStartFlag) {
            batch.begin();
            font.setColor(1, 1, 1, 1);
            font.draw(batch, TurnStartView.ALLY_TURN, TurnStartView.X, TurnStartView.Y);
            batch.end();
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
        choices[INDEX_ENEMY_SKILL_BUTTOM] = new ChoiceModel(ENEMY_SKILL_BUTTOM_X, BUTTOM_Y, ENEMY_SKILL_BUTTOM_WIDTH, BUTTOM_HEIGHT);
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

    public void releaseBall(int xPlace, int yPlace)
    {
        selectedBallView = null;
        battleScreenView.noFillBoard(PlayerEnum.Player1);
        if (xPlace == ChoiceListModel.NOT_CHOICED || yPlace == BoardModel.CAN_NOT_SET_BALL) {
            battleScreenView.displayBallInStack(PlayerEnum.Player1, true);
        }
    }

    public void moveBall(int x, int y)
    {
        selectedBallView.setPlace(x-SELECTED_BALL_SIZE/2, y-SELECTED_BALL_SIZE/2);
        battleScreenView.fillBoardOneLine(x, y, PlayerEnum.Player1);
    }

    public int getChoicedPlace(int x, int y)
    {
        int index = super.getChoicedPlace(x, y);
        if (index == INDEX_CANCEL_BUTTOM && !cancelButtomView.canPush()) {
            return ChoiceListModel.NOT_CHOICED;
        }
        if (index == ChoiceListModel.NOT_CHOICED && cardListView != null) {
            int magicIndex = cardListView.getChoicedPlace(x, y);
            selectedCardView = cardListView.getMagicCardView(magicIndex);
        }
        return index;
    }

    public boolean canShowEnemySkill()
    {
        return cardListView != null;
    }

    public void showEnemySkill(ActiveMagicModel[] magics)
    {
        cardListView = new MagicCardListView(ENEMY_MAGICS_START_X, ENEMY_MAGICS_START_Y, ENEMY_MAGICS_HORAIZONTAL_INTERVAL, ENEMY_MAGICS_VERTICAL_INTERVAL, magics);
    }

    public void hideEnemySkill()
    {
        cardListView = null;
        selectedCardView = null;
    }

    @Override
    public void display() {
        turnStartFlag = true;
        battleScreenView.setFilledAllowFlag(BattleScreen.ALLY_INDEX, true);
        battleScreenView.setFilledAllowFlag(BattleScreen.ENEMY_INDEX, true);
        battleScreenView.startBallsAnimationInStack(1, PlayerEnum.Player1);
    }

    @Override
    public void dispose() {
        turnStartFlag = false;
        battleScreenView.displayBallsnInStack(PlayerEnum.Player1);
        battleScreenView.setFilledAllowFlag(BattleScreen.ALLY_INDEX, false);
    }

    @Override
    public void animation(float time) {
        battleScreenView.ballsAnimationInStack(time, PlayerEnum.Player1);
    }

}
