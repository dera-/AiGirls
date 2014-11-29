package com.aigirls.screen.battle;

import com.aigirls.manager.ScreenManager;
import com.aigirls.model.battle.ActiveMagicModel;
import com.aigirls.model.battle.BallInfoModel;
import com.aigirls.model.battle.CharacterModel;
import com.aigirls.model.battle.EnemyCharacterModel;
import com.aigirls.param.ScreenEnum;
import com.aigirls.param.battle.PlayerEnum;
import com.aigirls.view.GameView;
import com.aigirls.view.battle.BattleScreenView;

public class AiTurnScreen extends TurnStartScreen {

    public AiTurnScreen(
        GameView view,
        CharacterModel attacker,
        CharacterModel defender,
        PlayerEnum attackerEnum,
        int totalBallCount)
    {
        super(view, attacker, defender, attackerEnum, totalBallCount);
    }

    @Override
    public void show() {
        super.show();
        ((EnemyCharacterModel) attacker).formatTemporaryParameters();
    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    protected void update(float delta) {
//        time += delta;
//        if(time < 2f) return;
//        time = 0;
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

    protected BattleScreenView getGameView()
    {
        return (BattleScreenView) view;
    }

    @Override
    protected BattleScreenView getBattleScreenView() {
        return getGameView();
    }
}
