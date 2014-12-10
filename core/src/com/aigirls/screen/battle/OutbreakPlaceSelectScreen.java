package com.aigirls.screen.battle;

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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

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
        int damageToBall = (int) Math.round(magic.getBallAttackRate() * players[BattleScreen.ALLY_INDEX].getMagicAttack());
        Array<ObstacleBallInfoModel[]> obstaclesList = new Array<ObstacleBallInfoModel[]>();
        //ダメージが与えられる邪魔玉を取得
        for (int i=0; i<magic.getNumTargetBalls(); i++) {
            obstaclesList.add(players[BattleScreen.ALLY_INDEX].getObstacleBallInfoModels(damageToBall, magic.getBallInfoModels(i)));
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
            Vector2 touchedPlace = getTouchedPlace(Gdx.input.getX(), Gdx.input.getY());
            int selected = getGameView().getChoicedPlace((int)touchedPlace.x, (int)touchedPlace.y);
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
