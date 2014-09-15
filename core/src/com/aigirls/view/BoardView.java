package com.aigirls.view;

import java.util.ArrayList;
import java.util.List;

import com.aigirls.config.GameConfig;
import com.aigirls.manager.TextureManager;
import com.aigirls.model.ChoiceListModel;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class BoardView extends SelectView{
    private final int interval;
    private final int sideWallWidth;
    private final int bottomWallHeight;
    private Sprite leftWallSprite;
    private Sprite rightWallSprite;
    private Sprite bottomWallSprite;
    private List<BallView> balls;

    public BoardView(int x, int y, int w, int h, int sideWallWidth, int bottomWallHeight)
    {
        super(x+sideWallWidth, y+bottomWallHeight, w-2*sideWallWidth, h-bottomWallHeight, GameConfig.BOARD_WIDTH);
        this.sideWallWidth = sideWallWidth;
        this.bottomWallHeight = bottomWallHeight;
        initializeTextures();
        interval = (int)Math.round(1.0*width/GameConfig.BOARD_WIDTH);
        balls = new ArrayList<BallView>();
    }

    private void initializeTextures(){
        leftWallSprite   = new Sprite(TextureManager.getTexture("side_wall"), leftX-sideWallWidth, lowerY, sideWallWidth, height);
        rightWallSprite  = new Sprite(TextureManager.getTexture("side_wall"), leftX+width, lowerY, sideWallWidth, height);
        bottomWallSprite = new Sprite(TextureManager.getTexture("bottom_wall"), leftX-sideWallWidth, lowerY-bottomWallHeight, width+2*sideWallWidth, bottomWallHeight);
    }

    public void addBall(int x, int y, String ballName)
    {
        BallView ball = new BallView(getX(x), getY(GameConfig.BOARD_HEIGHT), interval, interval, ballName);
        ball.dropBall(getY(y));
        balls.add(ball);
    }

    public void removeBall(int id)
    {
        for (BallView ball: balls) {
            if (id==ball.getBallId()) {
                balls.remove(ball);
                return;
            }
        }
    }

    public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer)
    {
        //ボールの描画
        for(BallView ball: balls){
            ball.draw(batch, shapeRenderer);
        }

        //壁の描画
        batch.begin();
        leftWallSprite.draw(batch);
        rightWallSprite.draw(batch);
        bottomWallSprite.draw(batch);
        batch.end();

        //区切り線の描画
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0.05f, 0.3f, 0.7f, 1);
        for (int i=0; i<choiceItemNums; i++) {
            int dx = i*interval;
            shapeRenderer.line(leftX+dx, lowerY, leftX+dx, lowerY+height);
        }
        shapeRenderer.end();
    }

    @Override
    protected ChoiceListModel getChoiceListModel() {
        return new ChoiceListModel(
                leftX,
                lowerY,
                width,
                height,
                choiceItemNums,
                interval,
                0
            );
    }

    private int getX(int x)
    {
        return leftX + x*interval;
    }

    private int getY(int y)
    {
        return lowerY + y*interval;
    }

}
