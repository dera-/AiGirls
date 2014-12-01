package com.aigirls.screen.battle;

import java.awt.Point;

import com.aigirls.manager.ScreenManager;
import com.aigirls.model.ChoiceListModel;
import com.aigirls.model.battle.BoardModel;
import com.aigirls.model.battle.CharacterModel;
import com.aigirls.param.ScreenEnum;
import com.aigirls.param.battle.PlayerEnum;
import com.aigirls.view.battle.BattleScreenView;
import com.aigirls.view.battle.PlayerTurnScreenView;
import com.badlogic.gdx.Gdx;

public class PlayerTurnScreen extends TurnStartScreen {

    private Point druggedPlace = null;

    public PlayerTurnScreen(
        BattleScreenView view,
        CharacterModel attacker,
        CharacterModel defender,
        PlayerEnum attackerEnum,
        int totalBallCount) {
        super(new PlayerTurnScreenView(view), attacker, defender, attackerEnum, totalBallCount);
        System.out.println("player turn");
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void hide() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    //TODO ドラッグandドロップの仕様にする
    protected void update(float delta) {
        if (Gdx.input.isTouched()) {
            Point touchedPlace = getTouchedPlace(Gdx.input.getX(), Gdx.input.getY());
            if (druggedPlace != null) {
                druggedEvent(touchedPlace.x, touchedPlace.y);
            } else if (Gdx.input.justTouched()) {
                touchedEvent(touchedPlace.x, touchedPlace.y);
            }
        } else if (druggedPlace != null) {
            releasedEvent();
        }
    }

    private void druggedEvent(int x, int y)
    {
        getGameView().moveBall(x, y);
        druggedPlace.move(x, y);
    }

    private void touchedEvent(int x, int y)
    {
        int index = getGameView().getChoicedPlace(x, y);
        switch(index) {
            case PlayerTurnScreenView.INDEX_BALL_STACK:
                if (!attacker.canReleaseBallFromStack()) {
                    return;
                }
                getGameView().pushOnBallStack(x, y);
                druggedPlace = new Point(x, y);
                return;
            case PlayerTurnScreenView.INDEX_CANCEL_BUTTOM:
                cancelPutBall();
                return;
            case PlayerTurnScreenView.INDEX_DECIDE_BUTTOM:
                MagicSelectScreen.setMagicSelectScreen(getBattleScreenView(), attacker.getActiveMagicModels());
                ScreenManager.changeScreen(ScreenEnum.GameAtMagicSelect);
                return;
            case PlayerTurnScreenView.INDEX_FINISH_BUTTOM:
                ScreenManager.changeScreen(ScreenEnum.GameAtFinishTurn);
                return;
        }
    }

    private void releasedEvent()
    {
        int xPlace = getBattleScreenView().getChoicedPlace(druggedPlace.x, druggedPlace.y);
        getGameView().releaseBall(xPlace);
        druggedPlace = null;
        if (xPlace == ChoiceListModel.NOT_CHOICED) {
            return;
        }
        int yPlace = attacker.getDropPlace(xPlace);
        if (yPlace == BoardModel.CAN_NOT_SET_BALL) {
            return;
        }
        dropBallEvent(xPlace, yPlace);
        getGameView().setCancelButtomFlag(true);
    }

    protected PlayerTurnScreenView getGameView()
    {
        return (PlayerTurnScreenView) view;
    }

    @Override
    protected BattleScreenView getBattleScreenView() {
        return getGameView().getBattleScreenView();
    }

    private void cancelPutBall()
    {
        for (int i = 1; i <= droppedBallNums; i++) {
            int id = totalBallCount + i;
            attacker.cancelAddingBall(id);
            attacker.addBallToStack(1);
            getBattleScreenView().removeBall(id, attackerEnum);
            getBattleScreenView().addToBallStack(attackerEnum);
            boolean existBall = defender.cancelAddingBall(id);
            if(existBall) {
                getBattleScreenView().removeBall(id, defenderEnum);
            }
        }
        defender.beHurt(-1*damageByDroppingBall);
        getBattleScreenView().moveHpBar(-1*damageByDroppingBall, defenderEnum);
        damageByDroppingBall = 0;
        droppedBallNums = 0;
        getGameView().setCancelButtomFlag(false);
    }

}
