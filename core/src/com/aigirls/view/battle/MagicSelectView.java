package com.aigirls.view.battle;

import com.aigirls.config.FileConfig;
import com.aigirls.config.GameConfig;
import com.aigirls.manager.BitmapFontManager;
import com.aigirls.manager.TextureManager;
import com.aigirls.model.ChoiceListModel;
import com.aigirls.model.ChoiceModel;
import com.aigirls.model.battle.ActiveMagicModel;
import com.aigirls.model.battle.ReturnCardModel;
import com.aigirls.screen.battle.BattleScreen;
import com.aigirls.view.MagicCardView;
import com.aigirls.view.SelectView;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class MagicSelectView extends SelectView {
    private static int START_X = (int) Math.round(0.5 * GameConfig.GAME_WIDTH);
    private static int START_Y = (int) Math.round(0.7 * GameConfig.GAME_HEIGHT);
    private static int HORAIZONTAL_INTERVAL = (int) Math.round(0.02 * GameConfig.GAME_WIDTH);
    private static int VERTICAL_INTERVAL = (int) Math.round(0.08 * GameConfig.GAME_HEIGHT);
    private static int CARD_NUMS = 6;
    private static int LINE_NUMS = 2;
    private static int ONE_LINE_CARD_NUMS = (int) Math.round(1.0*CARD_NUMS/LINE_NUMS);
    private static int CARD_WIDTH =
            (int)Math.round(1.0 * ((GameConfig.GAME_WIDTH-START_X) - ((ONE_LINE_CARD_NUMS+1)*HORAIZONTAL_INTERVAL)) / ONE_LINE_CARD_NUMS);
    private static final int NON_SELECTED = -1;
    private static ReturnCardModel returnCardModel = new ReturnCardModel();
    private static final int DECIDE_BUTTOM_X = (int) Math.round(0.6*GameConfig.GAME_WIDTH);
    private static final int CANCEL_BUTTOM_X = (int) Math.round(0.8*GameConfig.GAME_WIDTH);
    private static final int BUTTOM_Y = (int) Math.round(0.97*GameConfig.GAME_HEIGHT);
    private static final int BUTTOM_WIDTH = (int) Math.round(0.15*GameConfig.GAME_WIDTH);
    private static final int BUTTOM_HEIGHT = (int) Math.round(0.1*GameConfig.GAME_HEIGHT);

    private MagicCardView[] cards;
    private BattleScreenView battleScreenView;
    private BitmapFont font;
    private Sprite decideButtomSprite;
    private Sprite cancelButtomSprite;

    private int selectedIndex = NON_SELECTED;

    public MagicSelectView(BattleScreenView battleScreenView, ActiveMagicModel[] magics)
    {
        super(0, 0, GameConfig.GAME_WIDTH, GameConfig.GAME_HEIGHT, magics.length+1);
        cards = generateMagicCardViews(magics);
        choiceList = getChoiceListModel(cards);
        this.battleScreenView = battleScreenView;
        font = BitmapFontManager.getBitmapFont(FileConfig.NYANKO_FONT_KEY);
        decideButtomSprite = new Sprite(TextureManager.getTexture(FileConfig.BUTTOM_KEY));
        cancelButtomSprite = new Sprite(TextureManager.getTexture(FileConfig.BUTTOM_KEY));


    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        battleScreenView.draw(batch, shapeRenderer);
        for (MagicCardView cardView : cards) {
            cardView.draw(batch, shapeRenderer);
        }
        batch.begin();
        decideButtomSprite.setPosition(DECIDE_BUTTOM_X, BUTTOM_Y);
        decideButtomSprite.setSize(BUTTOM_WIDTH, BUTTOM_HEIGHT);
        decideButtomSprite.draw(batch);
        cancelButtomSprite.setPosition(CANCEL_BUTTOM_X, BUTTOM_Y);
        cancelButtomSprite.setSize(BUTTOM_WIDTH, BUTTOM_HEIGHT);
        cancelButtomSprite.draw(batch);
        batch.end();
        //技情報の描画
        if (selectedIndex == NON_SELECTED) {
            //font.draw(batch, "効果1：相手へ"+cards[selectedIndex].getAttackPercent()+"%攻撃", START_X, (int) Math.round(0.16 * GameConfig.GAME_HEIGHT));
            //font.draw(batch, "効果2:邪魔ボールへ"+cards[selectedIndex].getBallAttackPercent()+"%攻撃", START_X, (int) Math.round(0.08 * GameConfig.GAME_HEIGHT));
        } else {
            batch.begin();
            font.draw(batch, "効果1：相手へ"+cards[selectedIndex].getAttackPercent()+"%攻撃", START_X, (int) Math.round(0.16 * GameConfig.GAME_HEIGHT));
            font.draw(batch, "効果2:邪魔ボールへ"+cards[selectedIndex].getBallAttackPercent()+"%攻撃", START_X, (int) Math.round(0.08 * GameConfig.GAME_HEIGHT));
            batch.end();
        }
    }

    private MagicCardView[] generateMagicCardViews(ActiveMagicModel[] magics)
    {
        MagicCardView[] cardViews = new MagicCardView[magics.length+1];
        int x = START_X;
        int y = START_Y;
        for (int i=0; i<magics.length; i++) {
            cardViews[i] = new MagicCardView(x, y, CARD_WIDTH, magics[i]);
            if ((i%ONE_LINE_CARD_NUMS) == (ONE_LINE_CARD_NUMS-1)) {
                x = START_X;
                y -= (CARD_WIDTH + VERTICAL_INTERVAL);
            } else {
                x += (CARD_WIDTH + HORAIZONTAL_INTERVAL);
            }
        }
        cardViews[magics.length] = new MagicCardView(x, y, CARD_WIDTH, returnCardModel);
        return cardViews;
    }

    private ChoiceListModel getChoiceListModel(MagicCardView[] cardViews) {
        ChoiceModel[] choices = new ChoiceModel[cardViews.length];
        for (int i=0; i<choices.length; i++) {
            choices[i] = cardViews[i].getChoiceModel();
        }
        return new ChoiceListModel(choices);
    }

    protected ChoiceListModel getChoiceListModel() {
        return null;
    }

    public void filledEnemyView() {
        battleScreenView.filledDefenderView(BattleScreen.ENEMY_INDEX);
    }

}
