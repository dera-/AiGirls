package com.aigirls.screen.battle;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.aigirls.manager.ScreenManager;
import com.aigirls.model.battle.ActiveMagicModel;
import com.aigirls.model.battle.CharacterModel;
import com.aigirls.model.battle.ObstacleBallInfoModel;
import com.aigirls.param.ScreenEnum;
import com.aigirls.screen.GameScreen;
import com.aigirls.service.battle.DamageCalculateService;
import com.aigirls.view.battle.BattleScreenView;
import com.aigirls.view.battle.OutbreakPlaceSelectView;
import com.badlogic.gdx.Gdx;

public class OutbreakPlaceSelectScreen extends GameScreen {
    private static OutbreakPlaceSelectScreen screen;
    private CharacterModel[] players;

    private OutbreakPlaceSelectScreen(BattleScreenView battleScreenView, CharacterModel[] players) {
        super(new OutbreakPlaceSelectView(battleScreenView));
        this.players = players;
    }

    public static OutbreakPlaceSelectScreen getOutbreakPlaceSelectScreen()
    {
        return screen;
    }

    public static void setOutbreakPlaceSelectScreen(BattleScreenView battleScreenView, CharacterModel[] players)
    {
        screen = new OutbreakPlaceSelectScreen(battleScreenView, players);
    }

    public void setActiveMagicModel(ActiveMagicModel magic)
    {
        int damage = DamageCalculateService.getDamageValue(
                (int) Math.round(magic.getAttackRate() * players[BattleScreen.ALLY_INDEX].getAttack()),
                players[BattleScreen.ENEMY_INDEX].getDefense());
        List<ObstacleBallInfoModel[]> obstaclesList = new ArrayList<ObstacleBallInfoModel[]>();
        //ダメージが与えられる邪魔玉を取得
        for (int i=0; i<magic.getNumTargetBalls(); i++) {
            obstaclesList.add(players[BattleScreen.ALLY_INDEX].getObstacleBallInfoModels(damage, magic.getBallInfoModels(i)));
        }
        getGameView().setActiveMagicModel(magic);
        getGameView().setObstaclesList(obstaclesList);
        getGameView().setTemporaryDamage(damage);
    }

    @Override
    public void show() {
        getGameView().filledNothing();
        getGameView().setFlagsToBalls();
    }

    @Override
    public void hide() {
        getGameView().formatTemporaryParameters();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    protected void update(float delta) {
        if (Gdx.input.justTouched()) {
            Point touchedPlace = getTouchedPlace(Gdx.input.getX(), Gdx.input.getY());
            int selected = getGameView().getChoicedPlace(touchedPlace.x, touchedPlace.y);
            if (selected == OutbreakPlaceSelectView.DECIDE_BUTTOM_INDEX) {
                MagicOutbreakScreen.getMagicOutbreakScreen().setOutbrokenMagic(
                    BattleScreen.ALLY_INDEX,
                    getGameView().getActiveMagicModel(),
                    getGameView().getBallInfoModels());
                ScreenManager.changeScreen(ScreenEnum.GameAtMagicOutbreak);
            } else if (selected == OutbreakPlaceSelectView.CANCEL_BUTTOM_INDEX) {
                ScreenManager.changeScreen(ScreenEnum.GameAtActionReset);
            }
        }
    }

    protected OutbreakPlaceSelectView getGameView()
    {
        return (OutbreakPlaceSelectView) view;
    }

}
