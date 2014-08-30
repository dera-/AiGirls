package com.aigirls.model.battle;

import com.aigirls.entity.CharacterEntity;

public class CharacterModel
{
    private int attack;
    private int defense;
    private int magicAttack;
    private int magicDefense;
    private MagicModel[] magics;
    private BoardModel board;

    public CharacterModel(CharacterEntity entity){
        this.attack       = entity.attack;
        this.defense      = entity.defense;
        this.magicAttack  = entity.magicAttack;
        this.magicDefense = entity.magicDefense;
        //this.magics = magics;
        board = new BoardModel();
    }

    public int getAttack()
    {
        return attack;
    }

    public int getMagicDefense()
    {
        return magicDefense;
    }

    public void setBall(int x, BallModel ball)
    {
        board.setBall(x, ball);
    }

    public boolean canPutBall(int x)
    {
        return board.canSetBall(x);
    }

}
