package com.aigirls.model.battle;

import com.aigirls.entity.CharacterEntity;

public class CharacterModel
{
    private int maxHp;
    private int currentHp;
    private int attack;
    private int defense;
    private int magicAttack;
    private int magicDefense;
    private CharacterViewModel viewModel;
    private MagicModel[] magics;
    private BoardModel board;

    public CharacterModel(CharacterEntity entity, MagicModel[] magics){
        this.maxHp        = entity.hp;
        this.currentHp    = entity.hp;
        this.attack       = entity.attack;
        this.defense      = entity.defense;
        this.magicAttack  = entity.magicAttack;
        this.magicDefense = entity.magicDefense;
        this.viewModel = new CharacterViewModel(entity.name, entity.imageName);
        this.magics = magics;
        this.board = new BoardModel();
    }

    public int getMaxHp()
    {
        return maxHp;
    }

    public int getAttack()
    {
        return attack;
    }

    public int getDefense()
    {
        return defense;
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

    public int getDropPlace(int x)
    {
        return board.getDropPlace(x);
    }

    public boolean removeBall(int id)
    {
        return board.removeBall(id);
    }

    public CharacterViewModel getCharacterViewModel()
    {
        return viewModel;
    }

    public void beHurt(int damage) {
        this.currentHp -= damage;
    }

    public ActiveMagicModel[] getActiveMagicModels()
    {
        ActiveMagicModel[] activeMagicModels = new ActiveMagicModel[magics.length];
        BallModel[][] balls = board.getBalls();
        for (int i=0; i<activeMagicModels.length; i++) {
            activeMagicModels[i] = magics[i].generateActiveMagicModel(balls);
        }
        return activeMagicModels;
    }

}
