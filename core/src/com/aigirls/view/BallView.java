package com.aigirls.view;

import com.aigirls.config.FileConfig;
import com.aigirls.manager.TextureManager;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class BallView extends GameView {
    public final static int FLAG_MEAN_NORMAL = 0;
    public final static int FLAG_MEAN_TARGET = 1;
    public final static int FLAG_MEAN_SMALL_DAMAGE = 2;
    public final static int FLAG_MEAN_BIG_DAMAGE = 3;

    private final int id;
    private Sprite ballSprite;
    private Vector2 position;
    private Vector2 targetPosition;
    private int stateFlag = FLAG_MEAN_NORMAL;
    private boolean clearFlag = false;
    private Sprite starSprite;
    private Sprite smallDamageSprite;
    private Sprite bigDamageSprite;

    static {
        TextureManager.generateTexture(FileConfig.BALL1_IMAGE_PATH, FileConfig.BALL1_KEY);
        TextureManager.generateTexture(FileConfig.BALL2_IMAGE_PATH, FileConfig.BALL2_KEY);
        TextureManager.generateTexture(FileConfig.OBSTACLE_IMAGE_PATH, FileConfig.OBSTACLE_KEY);
    }

    public BallView(int id, int x,int y, int size, String name)
    {
        super(x, y, size, size);
        this.id = id;
        this.ballSprite = new Sprite(TextureManager.getTexture(name));
        this.position= new Vector2(x, y);
        this.targetPosition = new Vector2(x, y);
        starSprite = new Sprite(TextureManager.getTexture(FileConfig.STAR_KEY));
        smallDamageSprite = new Sprite(TextureManager.getTexture(FileConfig.SMALL_DAMAGE_KEY));
        bigDamageSprite = new Sprite(TextureManager.getTexture(FileConfig.BIG_DAMAGE_KEY));
    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        if (clearFlag) {
            return;
        }
        batch.begin();
        position.lerp(targetPosition, 0.2f);
        ballSprite.setPosition(position.x, position.y);
        ballSprite.setSize(width, height);
        ballSprite.draw(batch);
        drawState(batch);
        batch.end();
    }

    private void drawState(SpriteBatch batch)
    {
        Sprite sprite = null;
        switch (stateFlag) {
            case FLAG_MEAN_TARGET:
                sprite = starSprite;
                break;
            case FLAG_MEAN_SMALL_DAMAGE:
                sprite = smallDamageSprite;
                break;
            case FLAG_MEAN_BIG_DAMAGE:
                sprite = bigDamageSprite;
                break;
        }
        if (sprite == null) {
            return;
        }
        sprite.setPosition(position.x, position.y);
        sprite.setSize(width, height);
        sprite.draw(batch);
    }

    public void drop(int y)
    {
        targetPosition.set(targetPosition.x, y);
    }

    public void setPlace(int x, int y)
    {
        targetPosition.set(x, y);
    }

    public int getBallId(){
        return id;
    }

    public void setStateFlag(int flag)
    {
        this.stateFlag = flag;
    }

    public void setClearFlag(boolean clearFlag)
    {
        this.clearFlag = clearFlag;
    }

}
