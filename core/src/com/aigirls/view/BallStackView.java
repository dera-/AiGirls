package com.aigirls.view;

import com.aigirls.config.FileConfig;
import com.aigirls.config.GameConfig;
import com.aigirls.manager.BitmapFontManager;
import com.aigirls.model.ChoiceModel;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

public class BallStackView extends GameView {
    private final int wallWidth;
    private final int wallHeight;
    private final int interval;
    private Array<BallView> balls;
    private ChoiceModel choiceModel;
    private BitmapFont font;

    public BallStackView(int x, int y, int w, int h, int wallWidth, int wallHeight, String ballName)
    {
        super(x + wallWidth, y + wallHeight, w - 2*wallWidth, h - 2*wallHeight);
        this.wallWidth = wallWidth;
        this.wallHeight = wallHeight;
        this.interval = width / GameConfig.LIMIT_STACKED_BALL_NUM;
        balls = new Array<BallView>();
        for (int i=0; i<GameConfig.DEFAULT_STACKED_BALL_NUM; i++) {
            addBall(ballName);
        }
        choiceModel = new ChoiceModel(x, y, w, h);
        font = BitmapFontManager.getBitmapFont(FileConfig.NYANKO_FONT_KEY);
    }

    public void addBall(String ballName)
    {
        int nextIndex = balls.size;
        if (nextIndex >= GameConfig.LIMIT_STACKED_BALL_NUM) {
            return;
        }
        balls.add(new BallView(0, getX(nextIndex), lowerY, interval, ballName));
    }

    public void removeBall()
    {
        int lastIndex = balls.size-1;
        if (lastIndex < 0) {
            return;
        }
        balls.removeIndex(lastIndex);
    }

    public void displayBall(boolean displayFlag)
    {
        int lastIndex = balls.size-1;
        if (lastIndex < 0) {
            return;
        }
        balls.get(lastIndex).setClearFlag(!displayFlag);
    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer)
    {
        //ボールの描画
        for(BallView ball: balls){
            ball.draw(batch, shapeRenderer);
        }

        //壁の描画(塗りつぶし)
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.7f, 0.1f, 0.3f, 1);
        shapeRenderer.rect(leftX-wallWidth, lowerY, wallWidth, height);
        shapeRenderer.rect(leftX+width, lowerY, wallWidth, height);
        shapeRenderer.rect(leftX-wallWidth, lowerY-wallHeight, width+2*wallWidth, wallHeight);
        shapeRenderer.rect(leftX-wallWidth, lowerY+height, width+2*wallWidth, wallHeight);
        shapeRenderer.end();

        //壁の描画(枠線の描画)
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0, 0, 0, 1);
        shapeRenderer.rect(leftX-wallWidth, lowerY-wallHeight, width+2*wallWidth, height+2*wallHeight);
        shapeRenderer.end();

        //区切り線の描画
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0.05f, 0.3f, 0.7f, 1);
        for (int i=0; i<GameConfig.LIMIT_STACKED_BALL_NUM; i++) {
            int dx = i*interval;
            shapeRenderer.line(leftX+dx, lowerY, leftX+dx, lowerY+height);
        }
        shapeRenderer.end();

        //能力値増減の表示
        batch.begin();
        font.setColor(1, 1, 1, 1);
        double rate = GameConfig.STATUS_RATES[balls.size];
        String str = rate < 1 ? "減少" : "増加";
        int parsent = (int)Math.abs(rate*100 - 100);
        font.draw(batch, "能力値"+parsent+"%"+str, leftX, (int)(lowerY+1.55*(height+wallHeight)));
        batch.end();
    }

    public ChoiceModel getChoiceModel()
    {
        return choiceModel;
    }

    public void startBallsAnimation(int ballNums, String ballName)
    {
        int num = balls.size;
        for (int i=1; i<=ballNums; i++) {
            BallView ball = balls.get(num-i);
            ball.setClearFlag(true);
            ball.setAnimation();
        }
    }

    public void ballsAnimation(float time)
    {
        for (BallView ball : balls) {
            ball.animate(time);
        }
    }

    public void displayBalls()
    {
        for (BallView ball : balls) {
            ball.setClearFlag(false);
        }
    }

    private int getX(int x)
    {
        return leftX + x*interval;
    }

}
