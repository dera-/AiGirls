package com.aigirls.screen.battle;

import java.awt.Point;

import com.aigirls.manager.ScreenManager;
import com.aigirls.model.ChoiceListModel;
import com.aigirls.model.battle.BallModel;
import com.aigirls.model.battle.BoardModel;
import com.aigirls.model.battle.CharacterModel;
import com.aigirls.model.battle.ObstacleBallModel;
import com.aigirls.param.ScreenEnum;
import com.aigirls.param.battle.PlayerEnum;
import com.aigirls.screen.GameScreen;
import com.aigirls.service.battle.DamageCalculateService;
import com.aigirls.view.battle.BattleScreenView;
import com.badlogic.gdx.Gdx;

public class BattleScreen extends GameScreen
{
    private CharacterModel[] players;
    private int turnNum = 0;
    private int currentAttackerIndex = 0;
    private boolean putBall = false; //プレイヤーがボールを置いたかどうかの状態を一時的に保存するフラグ

    public BattleScreen(CharacterModel ally, CharacterModel enemy)
    {
        super(new BattleScreenView(ally.getCharacterViewModel(), enemy.getCharacterViewModel()));
        players = new CharacterModel[2];
        players[0] = ally;
        players[1] = enemy;
        ActionSelectScreen.setActionSelectScreen(getGameView());
        OutbreakPlaceSelectScreen.setOutbreakPlaceSelectScreen(getGameView());
    }

    @Override
    public void show() {
        if (currentAttackerIndex == 0 && putBall) {
            players[0].removeBall(turnNum);
            getGameView().removeBall(turnNum, getPlayerEnum(0));
            boolean existBall = players[1].removeBall(turnNum);
            if(existBall) {
                getGameView().removeBall(turnNum, getPlayerEnum(1));
            } else {
                int damage = DamageCalculateService.getDamageValue(
                    players[0].getAttack(),
                    players[1].getDefense());
                players[1].beHurt(-1*damage);
                double recoverRate = -1.0*damage/players[1].getMaxHp();
                getGameView().moveHpBar(recoverRate, getPlayerEnum(1));
            }
        }
        putBall = false;
    }

    @Override
    public void hide() {
        if (currentAttackerIndex == 0) {
            MagicSelectScreen.setMagicSelectScreen(getGameView(), players[0].getActiveMagicModels());
        }
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    protected void update(float delta) {
        if (currentAttackerIndex == 0 && Gdx.input.justTouched()) {
            Point touchedPlace = getTouchedPlace(Gdx.input.getX(), Gdx.input.getY());
            int xPlace = getGameView().getChoicedPlace(touchedPlace.x, touchedPlace.y);
            if (xPlace == ChoiceListModel.NOT_CHOICED) {
                return;
            }
            int yPlace = players[currentAttackerIndex].getDropPlace(xPlace);
            if (yPlace == BoardModel.CAN_NOT_SET_BALL) {
                return;
            }
            dropBallEvent(xPlace, yPlace);
            putBall = true;
            ScreenManager.changeScreen(ScreenEnum.GameAtActionSelect);
        }
    }

    protected BattleScreenView getGameView()
    {
        return (BattleScreenView) view;
    }

    public void nextTurn()
    {
        turnNum++;
        currentAttackerIndex = (currentAttackerIndex+1)%2;
    }

    private void dropBallEvent(int xPlace, int yPlace)
    {
        players[currentAttackerIndex].setBall(xPlace, new BallModel(turnNum));
        getGameView().dropBall(turnNum, xPlace, yPlace, getPlayerEnum(currentAttackerIndex));
        int defenserIndex = (currentAttackerIndex+1)%2;
        int defenserYPlace = players[defenserIndex].getDropPlace(xPlace);
        if (defenserYPlace == BoardModel.CAN_NOT_SET_BALL) {
            int damage = DamageCalculateService.getDamageValue(
                    players[currentAttackerIndex].getAttack(),
                    players[defenserIndex].getDefense());
            players[defenserIndex].beHurt(damage);
            double damageRate = 1.0*damage/players[defenserIndex].getMaxHp();
            getGameView().moveHpBar(damageRate, getPlayerEnum(defenserIndex));
        } else {
            players[defenserIndex].setBall(
                xPlace,
                new ObstacleBallModel(turnNum, players[currentAttackerIndex].getMagicDefense()));
            getGameView().dropObstacle(turnNum, xPlace, defenserYPlace, getPlayerEnum(defenserIndex));
        }
    }

    private PlayerEnum getPlayerEnum(int index)
    {
        if (index%2 == 0) {
            return PlayerEnum.Player1;
        } else {
            return PlayerEnum.Player2;
        }
    }

}
