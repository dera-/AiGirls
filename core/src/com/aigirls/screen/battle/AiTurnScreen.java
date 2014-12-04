package com.aigirls.screen.battle;

import com.aigirls.manager.ScreenManager;
import com.aigirls.model.battle.ActiveMagicModel;
import com.aigirls.model.battle.BallInfoModel;
import com.aigirls.model.battle.CharacterModel;
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
        ((EnemyCharacterModel) attacker).formatTemporaryParameters();
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
        int xPlace = aiChara.decidePutPlace(defender);
        dropBallEvent(xPlace, attacker.getDropPlace(xPlace));
        aiChara.useMagic(defender);
        ActiveMagicModel magic = aiChara.getMagicToUse();
        BallInfoModel[] balls = aiChara.getBallsToUse();
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
        System.out.println("enemyyyyyyyyyyyyyyyyyyyyyyyy");
        getGameView().display();
    }

    @Override
    protected void animation(float time) {
        System.out.println("enemy animation");
        getGameView().animation(time);
    }

    @Override
    protected void endAnimation() {
        System.out.println("enemy end");
        getGameView().dispose();
    }
}
