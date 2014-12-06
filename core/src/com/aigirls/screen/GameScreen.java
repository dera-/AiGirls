package com.aigirls.screen;

import java.awt.Point;

import com.aigirls.config.GameConfig;
import com.aigirls.view.GameView;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * このゲームで使用するスクリーンオブジェクト
 * カメラがデフォで入ってる。
 * @author onodera
 *
 */
public abstract class GameScreen implements Screen {
    protected OrthographicCamera camera;
    protected Viewport viewport;
    protected SpriteBatch batch;
    protected ShapeRenderer shapeRenderer;
    protected Vector3 touchPos;
    protected GameView view;

    public GameScreen(GameView view)
    {
        camera = new OrthographicCamera(GameConfig.GAME_WIDTH, GameConfig.GAME_HEIGHT);
        camera.setToOrtho(false, GameConfig.GAME_WIDTH, GameConfig.GAME_HEIGHT);
        viewport = new FitViewport(GameConfig.GAME_WIDTH, GameConfig.GAME_HEIGHT, camera);
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        touchPos = new Vector3();
        this.view = view;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    protected Point getTouchedPlace(int x, int y){
        touchPos.set(x, y ,0);
        viewport.unproject(touchPos);
        return new Point((int)Math.round(touchPos.x), (int)Math.round(touchPos.y));
    }

    @Override
    public void dispose()
    {
        batch.dispose();
        shapeRenderer.dispose();
    };

    @Override
    public void render(float delta)
    {
        update(delta);
        draw();
    }

    private void draw()
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);
        view.draw(batch, shapeRenderer);
    }

    protected abstract void update(float delta);

    protected GameView getGameView()
    {
        return view;
    }

}
