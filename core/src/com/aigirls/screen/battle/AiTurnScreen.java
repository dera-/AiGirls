package com.aigirls.screen.battle;

import com.aigirls.manager.ScreenManager;
import com.aigirls.model.battle.ActiveMagicModel;
import com.aigirls.model.battle.BallInfoModel;
import com.aigirls.model.battle.CharacterModel;
import com.aigirls.model.battle.EnemyActionModel;
import com.aigirls.model.battle.EnemyCharacterModel;
import com.aigirls.param.ScreenEnum;
import com.aigirls.param.battle.PlayerEnum;
import com.aigirls.view.battle.AiTurnScreenView;
import com.aigirls.view.battle.BattleScreenView;

public class AiTurnScreen extends TurnStartScreen {

    public AiTurnScreen(
        BattleScreenView view,
        CharacterModel attacker,
        CharacterModel defender,
        PlayerEnum attackerEnum,
        int totalBallCount)
    {
        super(new AiTurnScreenView(view), attacker, defender, attackerEnum, totalBallCount);
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
        EnemyCharacterModel aiChara = (EnemyCharacterModel) attacker;
        EnemyActionModel action = aiChara.getAction(defender, totalBallCount);
        for (BallInfoModel ball: action.targetBalls) {
            System.out.println("ID:"+ball.id);
        }
//        System.out.println(action.score);
        int[] xPlaces = action.putPlaces;
        for (int x : xPlaces) {
            dropBallEvent(x, attacker.getDropPlace(x));
        }
        ActiveMagicModel magic = action.magicModel;
        BallInfoModel[] balls = action.targetBalls;
        if (magic == null) {
            ScreenManager.changeScreen(ScreenEnum.GameAtFinishTurn);
        } else {
            MagicOutbreakScreen.getMagicOutbreakScreen().setOutbrokenMagic(BattleScreen.ENEMY_INDEX, magic, balls);
            ScreenManager.changeScreen(ScreenEnum.GameAtMagicOutbreak);
        }
    }

    protected AiTurnScreenView getGameView()
    {
        return (AiTurnScreenView) view;
    }

    @Override
    protected BattleScreenView getBattleScreenView() {
        return getGameView().getBattleScreenView();
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
