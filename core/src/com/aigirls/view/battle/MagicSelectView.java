package com.aigirls.view.battle;

import com.aigirls.config.FileConfig;
import com.aigirls.config.GameConfig;
import com.aigirls.manager.BitmapFontManager;
import com.aigirls.model.ChoiceListModel;
import com.aigirls.model.battle.ActiveMagicModel;
import com.aigirls.model.battle.ReturnCardModel;
import com.aigirls.param.battle.PlayerEnum;
import com.aigirls.screen.battle.BattleScreen;
import com.aigirls.view.CharacterView;
import com.aigirls.view.MagicCardListView;
import com.aigirls.view.MagicCardView;
import com.aigirls.view.SelectView;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class MagicSelectView extends SelectView {
    private static int START_X = (int) Math.round(0.5 * GameConfig.GAME_WIDTH);
    private static int START_Y = (int) Math.round(0.7 * GameConfig.GAME_HEIGHT);
    private static int HORAIZONTAL_INTERVAL = (int) Math.round(0.02 * GameConfig.GAME_WIDTH);
    private static int VERTICAL_INTERVAL = (int) Math.round(0.08 * GameConfig.GAME_HEIGHT);
    private static ReturnCardModel returnCardModel = new ReturnCardModel();

    private MagicCardListView cardListView;
    private MagicCardView seletedCard = null;
    private BattleScreenView battleScreenView;
    private BitmapFont font;

    public MagicSelectView(BattleScreenView battleScreenView, ActiveMagicModel[] magics)
    {
        super(0, 0, GameConfig.GAME_WIDTH, GameConfig.GAME_HEIGHT, magics.length+1);
        ActiveMagicModel[] newMagics = new ActiveMagicModel[magics.length+1];
        for (int i = 0; i< magics.length; i++) {
            newMagics[i] = magics[i];
        }
        newMagics[magics.length] = returnCardModel;
        cardListView = new MagicCardListView(START_X, START_Y, HORAIZONTAL_INTERVAL, VERTICAL_INTERVAL, newMagics);
        this.battleScreenView = battleScreenView;
        font = BitmapFontManager.getBitmapFont(FileConfig.NYANKO_FONT_KEY);
    }

    @Override
    public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        battleScreenView.draw(batch, shapeRenderer);
        cardListView.draw(batch, shapeRenderer);

        //技情報の描画
        if (seletedCard != null) {
            batch.begin();
            font.setColor(1f, 1f, 1f, 1f);
            font.draw(batch, seletedCard.displayUsableFlag(), START_X, (int) Math.round(0.32 * GameConfig.GAME_HEIGHT));
            font.draw(batch, "効果1：相手へ"+seletedCard.getAttackPercent()+"%攻撃", START_X, (int) Math.round(0.24 * GameConfig.GAME_HEIGHT));
            font.draw(batch, "効果2:邪魔玉へ"+seletedCard.getBallAttackPercent()+"%攻撃", START_X, (int) Math.round(0.16 * GameConfig.GAME_HEIGHT));
            font.draw(batch, "効果3:"+seletedCard.getRecoverBall()+"個ボール補充", START_X, (int) Math.round(0.08 * GameConfig.GAME_HEIGHT));
            batch.end();
        }
    }

    protected ChoiceListModel getChoiceListModel() {
        return null;
    }

    public int getChoicedPlace(int x, int y)
    {
        return cardListView.getChoicedPlace(x, y);
    }

    public void filledEnemyView() {
        battleScreenView.filledDefenderView(BattleScreen.ENEMY_INDEX);
    }

    public boolean isSelectedMagicCard ()
    {
        return seletedCard != null;
    }

    public void selectMagicCard(int index, int x, int y)
    {
        seletedCard = cardListView.getMagicCardView(index);
        seletedCard.setCardPlace(x, y);
        battleScreenView.changeCharaExpression(CharacterView.EXPRESSION_WAIT, PlayerEnum.Player1);
    }

    public void moveMagicCard(int x, int y)
    {
        seletedCard.setCardPlace(x, y);
        battleScreenView.fillBoard(x, y, PlayerEnum.Player1);
    }

    public void releaseMagicCard()
    {
        seletedCard.resetCardPlace();
        battleScreenView.noFillBoard(PlayerEnum.Player1);
        seletedCard = null;
    }

    public ActiveMagicModel getMagic()
    {
        Vector2 place = seletedCard.getCardPlace();
        if (battleScreenView.getChoicedPlace((int)place.x, (int)place.y) == ChoiceListModel.NOT_CHOICED) {
            battleScreenView.changeCharaExpression(CharacterView.EXPRESSION_NORMAL, PlayerEnum.Player1);
            return null;
        } else {
            return seletedCard.getMagicInfo();
        }
    }

}
