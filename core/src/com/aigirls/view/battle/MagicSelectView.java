package com.aigirls.view.battle;

import com.aigirls.config.GameConfig;
import com.aigirls.model.ChoiceListModel;
import com.aigirls.model.ChoiceModel;
import com.aigirls.model.battle.ActiveMagicModel;
import com.aigirls.view.FilledView;
import com.aigirls.view.MagicCardView;
import com.aigirls.view.SelectView;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class MagicSelectView extends SelectView {
    private static int START_X = (int) Math.round(0.1 * GameConfig.GAME_WIDTH);
    private static int START_Y = (int) Math.round(0.6 * GameConfig.GAME_HEIGHT);
    private static int HORAIZONTAL_INTERVAL = (int) Math.round(0.08 * GameConfig.GAME_WIDTH);
    private static int VERTICAL_INTERVAL = (int) Math.round(0.15 * GameConfig.GAME_HEIGHT);
    private static int CARD_NUMS = 6;
    private static int CARD_HALF_NUMS = (int) Math.round(1.0*CARD_NUMS/2);
    private static int CARD_WIDTH =
            (int)Math.round(1.0*(GameConfig.GAME_WIDTH - (2*START_X+(CARD_HALF_NUMS-1)*HORAIZONTAL_INTERVAL))/CARD_HALF_NUMS);
    private MagicCardView[] cards;
    private BattleScreenView battleScreenView;
    private FilledView filledView;

    public MagicSelectView(BattleScreenView battleScreenView, ActiveMagicModel[] magics)
    {
        super(0, 0, GameConfig.GAME_WIDTH, GameConfig.GAME_HEIGHT, magics.length+1);
        cards = generateMagicCardViews(magics);
        choiceList = getChoiceListModel(cards);
        this.battleScreenView = battleScreenView;
        filledView = new FilledView(0, 0, GameConfig.GAME_WIDTH, GameConfig.GAME_HEIGHT, new Color(0, 0, 0, 0.8f));
    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        battleScreenView.draw(batch, shapeRenderer);
        filledView.draw(batch, shapeRenderer);
        for (MagicCardView cardView : cards) {
            cardView.draw(batch, shapeRenderer);
        }
    }

    private MagicCardView[] generateMagicCardViews(ActiveMagicModel[] magics)
    {
        MagicCardView[] cardViews = new MagicCardView[magics.length+1];
        int x = START_X;
        int y = START_Y;
        for (int i=0; i<magics.length; i++) {
            cardViews[i] = new MagicCardView(x, y, CARD_WIDTH, magics[i]);
            if ((i%CARD_HALF_NUMS) == (CARD_HALF_NUMS-1)) {
                x = START_X;
                y -= (CARD_WIDTH + VERTICAL_INTERVAL);
            } else {
                x += (CARD_WIDTH + HORAIZONTAL_INTERVAL);
            }
        }
        cardViews[magics.length] = new MagicCardView(x, y, CARD_WIDTH, null);
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

}
