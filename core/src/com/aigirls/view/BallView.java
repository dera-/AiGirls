package com.aigirls.view;

import com.aigirls.config.FileConfig;
import com.aigirls.manager.TextureManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class BallView extends GameView {
    public final static int FLAG_MEAN_NORMAL = 0;
    public final static int FLAG_MEAN_TARGET = 1;
    public final static int FLAG_MEAN_SMALL_DAMAGE = 2;
    public final static int FLAG_MEAN_BIG_DAMAGE = 3;
    public final static int ID_IN_STACK = 0;
    private final static float DEFAULT_BALL_SPEED = 0.25f;
    private final static float SLOW_BALL_SPEED = 0.15f;

    private final int id;
    private Sprite ballSprite;
    private Vector2 position;
    private Vector2 targetPosition;
    private Animation ballAnimation = null;
    private int stateFlag = FLAG_MEAN_NORMAL;
    private boolean clearFlag = false;
    private Sprite starSprite;
    private Sprite smallDamageSprite;
    private Sprite bigDamageSprite;
    private float currentBallSPeed;
    private float animationTime = 0; //秒単位
    private String ballName;

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
        currentBallSPeed = DEFAULT_BALL_SPEED;
        ballName = name;
    }

    private Animation getBallAnimation()
    {
        if (ballName.equals(FileConfig.BALL1_KEY)) {
            if (id == ID_IN_STACK) {
                return new Animation(0.03f, TextureManager.getTextureRegion(FileConfig.BLUE_RECOVER_KEY));
            } else {
                return new Animation(0.05f, TextureManager.getTextureRegion(FileConfig.BLUE_STAR_KEY));
            }
        } else if (ballName.equals(FileConfig.BALL2_KEY)) {
            if (id == ID_IN_STACK) {
                return new Animation(0.03f, TextureManager.getTextureRegion(FileConfig.RED_RECOVER_KEY));
            } else {
                System.out.println("red star");
                return new Animation(0.05f, TextureManager.getTextureRegion(FileConfig.RED_STAR_KEY));
            }
        } else {
            return new Animation(0.05f, TextureManager.getTextureRegion(FileConfig.EXPLODE_KEY));
        }
    }

    private Animation getExplodedBallAnimation()
    {
        return new Animation(0.05f, TextureManager.getTextureRegion(FileConfig.EXPLODE_KEY));
    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        batch.begin();
        position.lerp(targetPosition, currentBallSPeed);
        ballSprite.setPosition(position.x, position.y);
        ballSprite.setSize(width, height);
        if (!clearFlag) {
            ballSprite.draw(batch);
            drawState(batch);
        }
        if (ballAnimation != null) {
            batch.draw(ballAnimation.getKeyFrame(animationTime),position.x, position.y, width, height);
        }
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

    public void changeBallSpeed(boolean slowly)
    {
        if (slowly) {
            currentBallSPeed = SLOW_BALL_SPEED;
        } else {
            currentBallSPeed = DEFAULT_BALL_SPEED;
        }
    }

    public void setAnimation() {
        ballAnimation = getBallAnimation();
    }

    public void startExploding() {
        ballAnimation = getExplodedBallAnimation();
    }

    public void animate(float m) {
        if (ballAnimation == null) {
            return;
        }

        if (ballAnimation.isAnimationFinished(animationTime)) {
            finishAnimation();
        }
        animationTime +=m;
    }

    public void finishAnimation() {
        ballAnimation = null;
        animationTime = 0;
    }

}
