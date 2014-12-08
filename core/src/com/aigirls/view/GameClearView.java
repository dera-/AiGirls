package com.aigirls.view;

import com.aigirls.config.FileConfig;
import com.aigirls.config.GameConfig;
import com.aigirls.manager.BitmapFontManager;
import com.aigirls.model.ChoiceListModel;
import com.aigirls.model.ChoiceModel;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class GameClearView extends SelectView{
    private static final int TITLE_BUTTOM_X = (int) Math.round(0.4*GameConfig.GAME_WIDTH);
    private static final int TITLE_BUTTOM_Y = (int) Math.round(0.1*GameConfig.GAME_HEIGHT);
    private static final int TITLE_BUTTOM_WIDTH = (int) Math.round(0.2*GameConfig.GAME_WIDTH);
    private static final int TITLE_BUTTOM_HEIGHT = (int) Math.round(0.15*GameConfig.GAME_HEIGHT);
    private static final int WAVING_WIDTH = (int) Math.round(0.2*GameConfig.GAME_WIDTH);
    private static final int WAVING_HEIGHT = (int) Math.round(0.3*GameConfig.GAME_HEIGHT);
    private static final int WAVING_X = (int) Math.round(0.4*GameConfig.GAME_WIDTH);
    private static final int WAVING_Y = (int) Math.round(0.35*GameConfig.GAME_HEIGHT);
    private static final int GAME_CLEAR_STRING_X = (int) Math.round(0.4*GameConfig.GAME_WIDTH);
    private static final int THANKS_STRING_X = (int) Math.round(0.2*GameConfig.GAME_WIDTH);
    private static final int GAME_CLEAR_STRING_Y = (int) Math.round(0.85*GameConfig.GAME_HEIGHT);
    private static final int THANKS_STRING_Y = (int) Math.round(0.7*GameConfig.GAME_HEIGHT);

    private static int SELECT_ITEM_NUMS = 1;
    public static int INDEX_TITLE_BUTTOM = 0;
    private WavingView wavingView;
    private ButtomView titleButtom;
    private String strengthString;
    private BitmapFont font;

    public GameClearView(int num)
    {
        super(0, 0, GameConfig.GAME_WIDTH, GameConfig.GAME_HEIGHT, SELECT_ITEM_NUMS);
        if (num < 0 || num >= GameConfig.STRENGTH_STRINGS.length) {
            strengthString = "";
        } else {
            strengthString = GameConfig.STRENGTH_STRINGS[num];
        }
        wavingView = new WavingView(WAVING_X, WAVING_Y, WAVING_WIDTH, WAVING_HEIGHT);
        titleButtom = new ButtomView(TITLE_BUTTOM_X, TITLE_BUTTOM_Y, TITLE_BUTTOM_WIDTH, TITLE_BUTTOM_HEIGHT, "タイトル");
        font = BitmapFontManager.getBitmapFont(FileConfig.NYANKO_FONT_KEY);
    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        titleButtom.draw(batch, shapeRenderer);
        wavingView.draw(batch, shapeRenderer);
        batch.begin();
        font.draw(batch, "ゲームクリア！！", GAME_CLEAR_STRING_X, GAME_CLEAR_STRING_Y);
        font.draw(batch, "今回の難易度は「"+strengthString+"」でした", THANKS_STRING_X, THANKS_STRING_Y);
        batch.end();
    }

    @Override
    protected ChoiceListModel getChoiceListModel() {
        ChoiceModel[] choices = new ChoiceModel[SELECT_ITEM_NUMS];
        choices[INDEX_TITLE_BUTTOM] = new ChoiceModel(TITLE_BUTTOM_X, TITLE_BUTTOM_Y, TITLE_BUTTOM_WIDTH, TITLE_BUTTOM_HEIGHT);
        return new ChoiceListModel(choices);
    }

    public void wavingAnimation(float time)
    {
        wavingView.animation(time);
    }
}
