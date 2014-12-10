package com.aigirls.screen;

import com.aigirls.manager.ScreenManager;
import com.aigirls.param.ScreenEnum;
import com.aigirls.view.GameClearView;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class GameClearScreen extends GameScreen {

    public GameClearScreen(int num) {
        super(new GameClearView(num));
    }

    @Override
    public void show() {}
    @Override
    public void hide() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    protected void update(float delta) {
        getGameView().wavingAnimation(delta);
        if (Gdx.input.justTouched()) {
            Vector2 touchedPlace = getTouchedPlace(Gdx.input.getX(), Gdx.input.getY());
            int selected = getGameView().getChoicedPlace((int)touchedPlace.x, (int)touchedPlace.y);
            if (selected == GameClearView.INDEX_TITLE_BUTTOM) {
                ScreenManager.changeScreen(ScreenEnum.GameAtReturnTitle);
            }
        }
    }

    protected GameClearView getGameView()
    {
        return (GameClearView) view;
    }

}
