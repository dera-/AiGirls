package com.aigirls.screen.battle;

import com.aigirls.manager.ScreenManager;
import com.aigirls.model.ChoiceListModel;
import com.aigirls.model.battle.BoardModel;
import com.aigirls.model.battle.CharacterModel;
import com.aigirls.param.ScreenEnum;
import com.aigirls.param.battle.PlayerEnum;
import com.aigirls.view.battle.BattleScreenView;
import com.aigirls.view.battle.PlayerTurnScreenView;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class PlayerTurnScreen extends TurnStartScreen {
    private Vector2 druggedPlace = null;

    public PlayerTurnScreen(
        BattleScreenView view,
        CharacterModel attacker,
        CharacterModel defender,
        PlayerEnum attackerEnum,
        int totalBallCount) {
        super(new PlayerTurnScreenView(view), attacker, defender, attackerEnum, totalBallCount);
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
    protected void action(float delta) {
        if (Gdx.input.isTouched()) {
            Vector2 touchedPlace = getTouchedPlace(Gdx.input.getX(), Gdx.input.getY());
            if (druggedPlace != null) {
                druggedEvent((int)touchedPlace.x, (int)touchedPlace.y);
            } else if (Gdx.input.justTouched()) {
                touchedEvent((int)touchedPlace.x, (int)touchedPlace.y);
            }
        } else if (druggedPlace != null) {
            releasedEvent();
        }
    }

    private void druggedEvent(int x, int y)
    {
        getGameView().moveBall(x, y);
        druggedPlace.set(x, y);
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
                druggedPlace = new Vector2(x, y);
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
            case PlayerTurnScreenView.INDEX_ENEMY_SKILL_BUTTOM:
                if (getGameView().canShowEnemySkill()) {
                    getGameView().hideEnemySkill();
                } else {
                    getGameView().showEnemySkill(defender.getActiveMagicModels());
                }
                return;
        }
    }

    private void releasedEvent()
    {
        int xPlace = getBattleScreenView().getChoicedPlace((int)druggedPlace.x, (int)druggedPlace.y);
        int yPlace = attacker.getDropPlace(xPlace);
        getGameView().releaseBall(xPlace, yPlace);
        druggedPlace = null;
        if (xPlace == ChoiceListModel.NOT_CHOICED || yPlace == BoardModel.CAN_NOT_SET_BALL) {
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

    @Override
    protected void startAnimation() {
        getGameView().display();
    }

    @Override
    protected void animation(float time) {
        getGameView().animation(time);
    }

    @Override
    protected void endAnimation() {
        getGameView().dispose();
    }

}
