package com.aigirls.screen.battle;

import java.awt.Point;

import com.aigirls.manager.ScreenManager;
import com.aigirls.model.ChoiceListModel;
import com.aigirls.model.battle.ActiveMagicModel;
import com.aigirls.model.battle.BallInfoModel;
import com.aigirls.model.battle.BallModel;
import com.aigirls.model.battle.BoardModel;
import com.aigirls.model.battle.CharacterModel;
import com.aigirls.model.battle.EnemyCharacterModel;
import com.aigirls.model.battle.ObstacleBallModel;
import com.aigirls.param.ScreenEnum;
import com.aigirls.param.battle.PlayerEnum;
import com.aigirls.screen.GameScreen;
import com.aigirls.service.battle.DamageCalculateService;
import com.aigirls.view.battle.BattleScreenView;
import com.badlogic.gdx.Gdx;

public class BattleScreen extends GameScreen
{
    public static final int ALLY_INDEX = 0;
    public static final int ENEMY_INDEX = 1;
    private CharacterModel[] players;
    private int turnNum = 0;
    private int currentAttackerIndex = 0;
    private boolean putBall = false; //プレイヤーがボールを置いたかどうかの状態を一時的に保存するフラグ

    public BattleScreen(CharacterModel ally, CharacterModel enemy)
    {
        super(new BattleScreenView(ally.getCharacterViewModel(), enemy.getCharacterViewModel()));
        players = new CharacterModel[2];
        players[ALLY_INDEX] = ally;
        players[ENEMY_INDEX] = enemy;
        ActionSelectScreen.setActionSelectScreen(getGameView());
        OutbreakPlaceSelectScreen.setOutbreakPlaceSelectScreen(getGameView());
        MagicOutbreakScreen.setMagicOutbreakScreen(getGameView(), players);
    }

    @Override
    public void show() {
        if (currentAttackerIndex == ALLY_INDEX && putBall) {
            players[ALLY_INDEX].removeBall(turnNum);
            getGameView().removeBall(turnNum, getPlayerEnum(ALLY_INDEX));
            boolean existBall = players[ENEMY_INDEX].removeBall(turnNum);
            if(existBall) {
                getGameView().removeBall(turnNum, getPlayerEnum(ENEMY_INDEX));
            } else {
                int damage = DamageCalculateService.getDamageValue(
                    players[ALLY_INDEX].getAttack(),
                    players[ENEMY_INDEX].getDefense());
                players[ENEMY_INDEX].beHurt(-1*damage);
                double recoverRate = -1.0*damage/players[ENEMY_INDEX].getMaxHp();
                getGameView().moveHpBar(recoverRate, getPlayerEnum(ENEMY_INDEX));
            }
        } else if (currentAttackerIndex == ENEMY_INDEX) {
            ((EnemyCharacterModel) players[ENEMY_INDEX]).formatTemporaryParameters();
        }
        putBall = false;
        getGameView().setDefenserIndex((currentAttackerIndex+1)%2);
    }

    @Override
    public void hide() {
        if (currentAttackerIndex == ALLY_INDEX) {
            MagicSelectScreen.setMagicSelectScreen(getGameView(), players[ALLY_INDEX].getActiveMagicModels());
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
        if (currentAttackerIndex == ALLY_INDEX && Gdx.input.justTouched()) {
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
        } else if (currentAttackerIndex == ENEMY_INDEX) {
            EnemyCharacterModel enemy = (EnemyCharacterModel) players[ENEMY_INDEX];
            int xPlace = enemy.decidePutPlace(players[ALLY_INDEX]);
            dropBallEvent(xPlace, players[currentAttackerIndex].getDropPlace(xPlace));
            enemy.useMagic(players[ALLY_INDEX]);
            ActiveMagicModel magic = enemy.getMagicToUse();
            BallInfoModel[] balls = enemy.getBallsToUse();
            if (magic == null) {
                ScreenManager.changeScreen(ScreenEnum.GameAtFinishTurn);
            } else {
                MagicOutbreakScreen.getMagicOutbreakScreen().setOutbrokenMagic(BattleScreen.ENEMY_INDEX, magic, balls);
                ScreenManager.changeScreen(ScreenEnum.GameAtMagicOutbreak);
            }
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
        getGameView().addBall(turnNum, xPlace, yPlace, getPlayerEnum(currentAttackerIndex));
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
            getGameView().addObstacle(turnNum, xPlace, defenserYPlace, getPlayerEnum(defenserIndex));
        }
    }

    private PlayerEnum getPlayerEnum(int index)
    {
        if (index%2 == ALLY_INDEX) {
            return PlayerEnum.Player1;
        } else {
            return PlayerEnum.Player2;
        }
    }

}
