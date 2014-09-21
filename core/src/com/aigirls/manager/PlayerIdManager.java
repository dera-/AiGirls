package com.aigirls.manager;

public class PlayerIdManager {
    private static int playerId = 1;

    public static void setPlayerId(int id)
    {
        playerId = id;
    }

    public static int getPlayerId()
    {
        return playerId;
    }
}
