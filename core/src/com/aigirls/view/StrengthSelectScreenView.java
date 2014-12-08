package com.aigirls.view;

import com.aigirls.config.GameConfig;
import com.aigirls.model.ChoiceListModel;
import com.aigirls.model.ChoiceModel;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class StrengthSelectScreenView extends SelectView {
    private static final int ITEM_WIDTH = (int) Math.round(0.5*GameConfig.GAME_WIDTH);
    private static final int ITEM_HEIGHT = (int) Math.round(0.18*GameConfig.GAME_HEIGHT);
    private static final int ITEM_START_X = (int) Math.round(0.1*GameConfig.GAME_WIDTH);
    private static final int ITEM_START_Y = (int) Math.round(0.03*GameConfig.GAME_HEIGHT);
    private static final int ITEM_INTERVAL = (int) Math.round(0.02*GameConfig.GAME_HEIGHT);
    private static final int ITEM_NUMS = 3;
    private static final int WAVING_WIDTH = (int) Math.round(0.2*GameConfig.GAME_WIDTH);
    private static final int WAVING_HEIGHT = (int) Math.round(0.3*GameConfig.GAME_HEIGHT);
    private static final int WAVING_X = (int) Math.round(0.7*GameConfig.GAME_WIDTH);
    private static final int WAVING_Y = (int) Math.round(0.2*GameConfig.GAME_HEIGHT);
    private static String[] strengths = {"つよい", "ふつう", "よわい"};
    private static Color[] colors = {Color.RED, Color.GREEN, Color.BLUE};
    private static Color fontColor = new Color(1,1,1,1);
    public static final int STRENGTH_STRONG = 0;
    public static final int STRENGTH_NORMAL = 1;
    public static final int STRENGTH_WEAK   = 2;

    private ButtomView[] items = new ButtomView[ITEM_NUMS];
    private WavingView waving = null;

    public StrengthSelectScreenView()
    {
        super(0, 0, GameConfig.GAME_WIDTH, GameConfig.GAME_HEIGHT, ITEM_NUMS);
        for (int i=0; i<ITEM_NUMS; i++) {
            items[i] = new ButtomView(ITEM_START_X, ITEM_START_Y+i*(ITEM_HEIGHT+ITEM_INTERVAL), ITEM_WIDTH, ITEM_HEIGHT, strengths[i], colors[i], fontColor);
        }
        waving = new WavingView(WAVING_X, WAVING_Y, WAVING_WIDTH, WAVING_HEIGHT);
    }

    @Override
    protected ChoiceListModel getChoiceListModel() {
        ChoiceModel[] choices = new ChoiceModel[ITEM_NUMS];
        for (int i=0; i<ITEM_NUMS; i++) {
            choices[i] = new ChoiceModel(ITEM_START_X, ITEM_START_Y+i*(ITEM_HEIGHT+ITEM_INTERVAL), ITEM_WIDTH, ITEM_HEIGHT);
        }
        return new ChoiceListModel(choices);
    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        waving.draw(batch, shapeRenderer);
        for (ButtomView item : items) {
            item.draw(batch, shapeRenderer);
        }
    }

    public void wavingAnimation(float time)
    {
        waving.animation(time);
    }

}
