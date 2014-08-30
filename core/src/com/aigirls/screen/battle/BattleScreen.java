package com.aigirls.screen.battle;

import java.awt.Point;

import com.aigirls.entity.CharacterEntity;
import com.aigirls.entity.GirlEntity;
import com.aigirls.entity.MonsterEntity;
import com.aigirls.manager.ScreenManager;
import com.aigirls.model.battle.BallModel;
import com.aigirls.model.battle.CharacterModel;
import com.aigirls.model.battle.ObstacleBallModel;
import com.aigirls.param.ScreenEnum;
import com.aigirls.param.battle.EnemyType;
import com.aigirls.screen.GameScreen;
import com.aigirls.view.GameView;
import com.aigirls.view.battle.BattleScreenView;
import com.badlogic.gdx.Gdx;

public class BattleScreen extends GameScreen
{
    private CharacterModel[] players;
    private BattleScreenView viewer;
    private int turnNum = 0;
    private int currentAttackerIndex = 0;

    public BattleScreen(CharacterEntity allyEntity, CharacterEntity enemyEntity)
    {
        super(new BattleScreenView(allyEntity.imageName, enemyEntity.imageName));
        players = new CharacterModel[2];
        players[0] = new CharacterModel(allyEntity);
        players[1] = new CharacterModel(enemyEntity);
        ActionSelectScreen.setActionSelectScreen(getBattleScreenView());
    }

    private CharacterEntity getEnemyEntity(int enemyId, EnemyType type)
    {
        switch(type){
            case PLAYER:
                return new GirlEntity(enemyId);
            case MONSTER:
                return new MonsterEntity(enemyId);
            default:
                return null;
        }
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
    public void dispose() {
        super.dispose();
    }

    @Override
    protected void update(float delta) {
        if (Gdx.input.isTouched()) {
            Point touchedPlace = getTouchedPlace(Gdx.input.getX(), Gdx.input.getY());
            int xPlace = viewer.getChoicedPlace(touchedPlace.x, touchedPlace.y);
            if (players[0].canPutBall(xPlace)) {
                players[0].setBall(xPlace, new BallModel(turnNum));
                players[1].setBall(xPlace, new ObstacleBallModel(turnNum, players[0].getMagicDefense()));
                ScreenManager.changeScreen(ScreenEnum.GameAtActionSelect);
            }
        }
    }

    private BattleScreenView getBattleScreenView()
    {
        return (BattleScreenView) view;
    }

}
