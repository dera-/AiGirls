package com.aigirls.screen.battle;

import com.aigirls.factory.BattleScreenFactory;
import com.aigirls.manager.ScreenManager;
import com.aigirls.param.ScreenEnum;
import com.aigirls.param.battle.EnemyType;
import com.aigirls.screen.GameScreen;
import com.aigirls.view.battle.LoadingBattleScreenView;

public class LoadingBattleScreen extends GameScreen {
    private static final float LOAD_START_TIME = 0.5f;
    private int enemyId;
    private EnemyType enemyType;
    private float time = 0;

    public LoadingBattleScreen(int enemyId, EnemyType enemyType) {
        super(new LoadingBattleScreenView());
        this.enemyId = enemyId;
        this.enemyType = enemyType;
    }

    @Override
    public void show() {}

    @Override
    public void hide() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    protected void update(float delta) {
        if (time < LOAD_START_TIME) {
            time += delta;
            return;
        }
        BattleScreen screen = BattleScreenFactory.generateBattleScreen(enemyId, enemyType);
        ScreenManager.setBattleScreen(screen);
        ScreenManager.changeScreen(ScreenEnum.StartBattle);
    }

}
