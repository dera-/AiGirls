package com.aigirls.view;

import com.aigirls.config.GameConfig;
import com.aigirls.model.ChoiceListModel;
import com.aigirls.model.ChoiceModel;
import com.aigirls.model.battle.ActiveMagicModel;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class MagicCardListView extends SelectView {
    private static final int LINE_NUMS = 2;
    private int startX;
    private int startY;
    private final int horaizontalInterval;
    private final int verticalInterval;
    private final int cardNums;
    private MagicCardView[] cards;
    private static int oneLineCardNums;
    private static int cardWidth;

    public MagicCardListView(int x, int y, int horaizontal, int vertical, ActiveMagicModel[] magics) {
        super(0, 0,  GameConfig.GAME_WIDTH, GameConfig.GAME_HEIGHT, magics.length);
        startX = x;
        startY = y;
        horaizontalInterval = horaizontal;
        verticalInterval = vertical;
        cardNums = magics.length;
        oneLineCardNums = (int) Math.round(1.0*cardNums/LINE_NUMS);
        cardWidth = (int)Math.round(1.0 * ((GameConfig.GAME_WIDTH - startX) - ((oneLineCardNums+1)*horaizontalInterval)) / oneLineCardNums);
        cards = generateMagicCardViews(magics);
        choiceList = getChoiceListModel(cards);
    }

    private MagicCardView[] generateMagicCardViews(ActiveMagicModel[] magics)
    {
        MagicCardView[] cardViews = new MagicCardView[magics.length];
        int x = startX;
        int y = startY;
        for (int i=0; i<magics.length; i++) {
            cardViews[i] = new MagicCardView(x, y, cardWidth, magics[i]);
            if ((i%oneLineCardNums) == (oneLineCardNums-1)) {
                x = startX;
                y -= (cardWidth + verticalInterval);
            } else {
                x += (cardWidth + horaizontalInterval);
            }
        }
        return cardViews;
    }

    @Override
    protected ChoiceListModel getChoiceListModel() {
        return null;
    }

    private ChoiceListModel getChoiceListModel(MagicCardView[] cardViews) {
        ChoiceModel[] choices = new ChoiceModel[cardViews.length];
        for (int i=0; i<choices.length; i++) {
            choices[i] = cardViews[i].getChoiceModel();
        }
        return new ChoiceListModel(choices);
    }

    public MagicCardView getMagicCardView(int index)
    {
        return (0<=index && index < cards.length) ? cards[index] : null;
    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        for (MagicCardView cardView : cards) {
            cardView.draw(batch, shapeRenderer);
        }
    }

}
