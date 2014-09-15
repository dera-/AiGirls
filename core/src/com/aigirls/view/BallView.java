package com.aigirls.view;

import com.aigirls.config.FileConfig;
import com.aigirls.manager.TextureManager;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class BallView extends GameView {
    private final int id;
    private Sprite ballSprite;
    private Vector2 position;
    private Vector2 targetPosition;

    static {
        TextureManager.generateTexture(FileConfig.BALL1_FILE_PATH, FileConfig.BALL1_KEY);
        TextureManager.generateTexture(FileConfig.BALL2_FILE_PATH, FileConfig.BALL2_KEY);
        TextureManager.generateTexture(FileConfig.OBSTACLE_FILE_PATH, FileConfig.OBSTACLE_KEY);
    }

    public BallView(int id, int x,int y, int size, String name)
    {
        super(x, y, size, size);
        this.id = id;
        this.ballSprite = new Sprite(TextureManager.getTexture(name), width, height);
        this.position= new Vector2(x, y);
        this.targetPosition = new Vector2(x, y);
        this.ballSprite.setPosition(this.position.x, this.position.y);
    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        batch.begin();
        position.lerp(targetPosition, 0.2f);
        ballSprite.setPosition(position.x, position.y);
        ballSprite.draw(batch);
        batch.begin();
    }

    public void dropBall(int y){
        targetPosition.set(targetPosition.x, y);
    }

    public int getBallId(){
        return id;
    }

}
