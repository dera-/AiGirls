package com.aigirls.view;

import com.aigirls.config.FileConfig;
import com.aigirls.manager.TextureManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class WavingView extends GameView {
    private static TextureRegion[] textureRegions;

    private Animation charaAnime;
    private float currentTime = 0;

    static {
        String[] paths = {
                FileConfig.WAVING_IMAGE_PATH_0,
                FileConfig.WAVING_IMAGE_PATH_1,
                FileConfig.WAVING_IMAGE_PATH_2,
                FileConfig.WAVING_IMAGE_PATH_3,
                FileConfig.WAVING_IMAGE_PATH_4,
                FileConfig.WAVING_IMAGE_PATH_3,
                FileConfig.WAVING_IMAGE_PATH_2,
                FileConfig.WAVING_IMAGE_PATH_1
        };
        String[] keys = {
                FileConfig.WAVING_KEY_0,
                FileConfig.WAVING_KEY_1,
                FileConfig.WAVING_KEY_2,
                FileConfig.WAVING_KEY_3,
                FileConfig.WAVING_KEY_4,
                FileConfig.WAVING_KEY_3,
                FileConfig.WAVING_KEY_2,
                FileConfig.WAVING_KEY_1
        };
        textureRegions = TextureManager.getTextureRegions(paths, keys, 8);
    }

    public WavingView(int x, int y, int w, int h)
    {
        super(x, y, w, h);
        charaAnime = new Animation(0.1f, textureRegions);
    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer)
    {
        batch.begin();
        batch.draw(charaAnime.getKeyFrame(currentTime, true), leftX, lowerY, width, height);
        batch.end();
    }

    public void animation(float time)
    {
        currentTime = (currentTime + time) % 1000f;
    }

}
