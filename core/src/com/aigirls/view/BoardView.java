package com.aigirls.view;

import java.util.ArrayList;
import java.util.List;

import com.aigirls.config.FileConfig;
import com.aigirls.config.GameConfig;
import com.aigirls.manager.TextureManager;
import com.aigirls.model.ChoiceListModel;
import com.aigirls.model.battle.BallInfoModel;
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

    static {
        TextureManager.generateTexture(FileConfig.SIDE_WALL_IMAGE_PATH, FileConfig.SIDE_WALL_KEY);
        TextureManager.generateTexture(FileConfig.BOTTOM_WALL_IMAGE_PATH, FileConfig.BOTTOM_WALL_KEY);
    }

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
        leftWallSprite   = new Sprite(TextureManager.getTexture(FileConfig.SIDE_WALL_KEY));
        rightWallSprite  = new Sprite(TextureManager.getTexture(FileConfig.SIDE_WALL_KEY));
        bottomWallSprite = new Sprite(TextureManager.getTexture(FileConfig.BOTTOM_WALL_KEY));
    }

    public void addBall(int id, int x, int y, String ballName)
    {
        BallView ball = new BallView(id, getX(x), getY(GameConfig.BOARD_HEIGHT), interval, ballName);
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

    public void setTargetFlags(BallInfoModel[] ballsInfo)
    {
        initializeTargetFlags();
        for (BallInfoModel info : ballsInfo) {
            for (BallView ball: balls) {
                if (info.id == ball.getBallId()) {
                    ball.setTargetFlag(true);
                    break;
                }
            }
        }
    }

    public void initializeTargetFlags()
    {
        for (BallView ball: balls) {
            ball.setTargetFlag(false);
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
        leftWallSprite.setSize(sideWallWidth, height);
        leftWallSprite.setPosition(leftX-sideWallWidth, lowerY);
        leftWallSprite.draw(batch);
        rightWallSprite.setSize(sideWallWidth, height);
        rightWallSprite.setPosition(leftX+width, lowerY);
        rightWallSprite.draw(batch);
        bottomWallSprite.setSize(width+2*sideWallWidth, bottomWallHeight);
        bottomWallSprite.setPosition(leftX-sideWallWidth, lowerY-bottomWallHeight);
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
                (int)Math.round(1.0*width/GameConfig.BOARD_WIDTH),
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
