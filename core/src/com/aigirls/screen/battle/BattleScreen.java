package com.aigirls.screen.battle;

import com.aigirls.model.battle.CharacterModel;
import com.aigirls.model.battle.EnemyCharacterModel;
import com.aigirls.param.battle.PlayerEnum;
import com.aigirls.screen.GameScreen;
import com.aigirls.view.battle.BattleScreenView;

public class BattleScreen extends GameScreen
{
    public static final int ALLY_INDEX = 0;
    public static final int ENEMY_INDEX = 1;
    private CharacterModel[] players;
    private int currentAttackerIndex = 0;
    private int turnNum = 1;
    private int totalBallCount = 0;
    private TurnStartScreen turnStartScreen = null;

    public BattleScreen(CharacterModel ally, CharacterModel enemy)
    {
        super(new BattleScreenView(ally.getCharacterViewModel(), enemy.getCharacterViewModel()));
        players = new CharacterModel[2];
        players[ALLY_INDEX] = ally;
        players[ENEMY_INDEX] = enemy;
        OutbreakPlaceSelectScreen.setOutbreakPlaceSelectScreen(getGameView(), players);
        MagicOutbreakScreen.setMagicOutbreakScreen(getGameView(), players);
        BattleEndScreen.setBattleEndScreen(getGameView(), players);
    }

    public void nextTurn()
    {
        turnNum++;
        currentAttackerIndex = (currentAttackerIndex+1)%2;
        if (turnStartScreen != null) {
            totalBallCount += turnStartScreen.getDroppedBallNums();
        }
    }

    public GameScreen getTurnStartScreen()
    {
        CharacterModel attacker = players[currentAttackerIndex];
        CharacterModel defender = players[(currentAttackerIndex+1)%2];

        if (players[currentAttackerIndex] instanceof EnemyCharacterModel) {
            turnStartScreen = new AiTurnScreen(getGameView(), attacker, defender, getPlayerEnum(currentAttackerIndex), totalBallCount);
        } else {
            turnStartScreen = new PlayerTurnScreen(getGameView(), attacker, defender, getPlayerEnum(currentAttackerIndex), totalBallCount);
        }
        return turnStartScreen;
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

    protected BattleScreenView getGameView()
    {
        return (BattleScreenView) view;
    }

    @Override
    protected void update(float delta) {}

    private PlayerEnum getPlayerEnum(int index)
    {
        if (index%2 == ALLY_INDEX) {
            return PlayerEnum.Player1;
        } else {
            return PlayerEnum.Player2;
        }
    }

}
