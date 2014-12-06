package com.aigirls.manager;

import java.util.Stack;

import com.aigirls.factory.BattleScreenFactory;
import com.aigirls.param.ScreenEnum;
import com.aigirls.param.battle.EnemyType;
import com.aigirls.screen.GameStartScreen;
import com.aigirls.screen.StrengthSelectScreen;
import com.aigirls.screen.battle.BattleEndScreen;
import com.aigirls.screen.battle.BattleScreen;
import com.aigirls.screen.battle.MagicOutbreakScreen;
import com.aigirls.screen.battle.MagicSelectScreen;
import com.aigirls.screen.battle.OutbreakPlaceSelectScreen;
import com.badlogic.gdx.Screen;

public class ScreenManager {
    private static Stack<Screen> screenStack = new Stack<Screen>();
    private static int enemyNumber = 0;

    public static void changeScreen(ScreenEnum screenEnum)
    {
        switch (screenEnum) {
            case StartGame:
                screenStack.push(GameStartScreen.getGameStartScreen());
                break;
            case StrengthSelect:
                screenStack.push(StrengthSelectScreen.getStrengthSelect());
                break;
            case StartBattle:
                startBattle();
                break;
            case GameAtMagicSelect:
                screenStack.push(MagicSelectScreen.getMagicSelectScreen());
                break;
            case GameAtOutbreakSelect:
                screenStack.push(OutbreakPlaceSelectScreen.getOutbreakPlaceSelectScreen());
                break;
            case GameAtMagicOutbreak:
                screenStack.push(MagicOutbreakScreen.getMagicOutbreakScreen());
                break;
            case GameAtActionReset:
                screenStack.pop();
                break;
            case GameAtFinishTurn:
                deleteScreensUntilBattleScreen();
                Screen screen = getNowScreen();
                if (screen instanceof BattleScreen) {
                    BattleScreen battleScreen = (BattleScreen) screen;
                    battleScreen.nextTurn();
                    screenStack.push(battleScreen.getTurnStartScreen());
                }
                break;
            case GameAtBattleEnd:
                screenStack.push(BattleEndScreen.getBattleEndScreen());
                break;
            case GameAtRetry:
                deleteCureentBattleScreen();
                startBattle();
                break;
            case GameAtReturnTitle:
                deleteCureentBattleScreen();
                screenStack.push(GameStartScreen.getGameStartScreen());
                break;
            case GameAtNext:
                deleteCureentBattleScreen();
                setNumber(enemyNumber+1);
                startBattle();
                break;
            default:
                break;
        }
    }

    private static void startBattle()
    {
        BattleScreen battlescreen = BattleScreenFactory.generateBattleScreen(enemyNumber, EnemyType.MONSTER);
        screenStack.push(battlescreen);
        screenStack.push(battlescreen.getTurnStartScreen());
    }

    private static void deleteScreensUntilBattleScreen()
    {
        while (!(screenStack.peek() instanceof BattleScreen)) {
            screenStack.pop();
        }
    }

    private static void deleteCureentBattleScreen()
    {
        deleteScreensUntilBattleScreen();
        Screen screen = getNowScreen();
        if (screen instanceof BattleScreen) {
            BattleScreen battleScreen = (BattleScreen) screen;
            battleScreen.dispose();
            screenStack.pop();
        }
    }

    public static Screen getNowScreen()
    {
        return screenStack.peek();
    }

    public static void setNumber(int num)
    {
        enemyNumber = num;
    }

}
