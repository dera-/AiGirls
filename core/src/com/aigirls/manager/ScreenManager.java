package com.aigirls.manager;

import java.util.Stack;

import com.aigirls.factory.BattleScreenFactory;
import com.aigirls.param.ScreenEnum;
import com.aigirls.param.battle.EnemyType;
import com.aigirls.screen.battle.BattleScreen;
import com.aigirls.screen.battle.MagicOutbreakScreen;
import com.aigirls.screen.battle.MagicSelectScreen;
import com.aigirls.screen.battle.OutbreakPlaceSelectScreen;
import com.badlogic.gdx.Screen;

public class ScreenManager {
    private static Stack<Screen> screenStack = new Stack<Screen>();

    public static void changeScreen(ScreenEnum screenEnum)
    {
        switch (screenEnum) {
            case StartBattle:
                BattleScreen battlescreen = BattleScreenFactory.generateBattleScreen(1, EnemyType.MONSTER);
                screenStack.push(battlescreen);
                screenStack.push(battlescreen.getTurnStartScreen());
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
            default:
                break;
        }
    }

    private static void deleteScreensUntilBattleScreen()
    {
        while (!(screenStack.peek() instanceof BattleScreen)) {
            screenStack.pop();
        }
    }


    public static Screen getNowScreen()
    {
        return screenStack.peek();
    }


}
