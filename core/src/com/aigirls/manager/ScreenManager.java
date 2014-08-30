package com.aigirls.manager;

import java.util.Stack;

import com.aigirls.param.ScreenEnum;
import com.aigirls.screen.battle.ActionSelectScreen;
import com.badlogic.gdx.Screen;

public class ScreenManager {
    private static Stack<Screen> screenStack = new Stack<Screen>();

    public static void changeScreen(ScreenEnum screenEnum)
    {
        switch (screenEnum) {
            case StartBattle:

                break;
            case GameAtActionSelect:
                screenStack.push(ActionSelectScreen.getActionSelectScreen());
                break;
            default:
                break;
        }
    }


    public static Screen getNowScreen()
    {
        return screenStack.get(0);
    }


}
