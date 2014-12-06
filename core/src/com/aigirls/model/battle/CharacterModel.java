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
    protected MagicModel[] magics;
    protected BoardModel board;
    protected BallStackModel ballStack;

    public CharacterModel(CharacterEntity entity, MagicModel[] magics)
    {
        this.maxHp        = entity.hp;
        this.currentHp    = entity.hp;
        this.attack       = entity.attack;
        this.defense      = entity.defense;
        this.magicAttack  = entity.magicAttack;
        this.magicDefense = entity.magicDefense;
        this.viewModel = new CharacterViewModel(entity.name, entity.imageName, entity.hp);
        this.magics = magics;
        this.board = new BoardModel();
        this.ballStack = new BallStackModel();
    }

    private CharacterModel(CharacterModel model)
    {
        this.maxHp        = model.maxHp;
        this.currentHp    = model.currentHp;
        this.attack       = model.attack;
        this.defense      = model.defense;
        this.magicAttack  = model.magicAttack;
        this.magicDefense = model.magicDefense;
        this.magics       = model.magics;
        this.board        = model.board.getClone();
        this.ballStack    = model.ballStack.getClone();
    }

    public CharacterModel getClone()
    {
        return new CharacterModel(this);
    }

    public int getCurrentHp()
    {
        return currentHp;
    }

    public int getMaxHp()
    {
        return maxHp;
    }

    public int getAttack()
    {
        return (int) Math.round(attack * ballStack.getStatusRate());
    }

    public int getDefense()
    {
        return (int) Math.round(defense * ballStack.getStatusRate());
    }

    public int getMagicAttack()
    {
        return (int) Math.round(magicAttack * ballStack.getStatusRate());
    }

    public int getMagicDefense()
    {
        return (int) Math.round(magicDefense * ballStack.getStatusRate());
    }

    public int getStackedBallNum()
    {
        return ballStack.getStackedBallNum();
    }

    public void addBallToStack(int num)
    {
        ballStack.addBall(num);
    }

    public void removeBallFromStack()
    {
        ballStack.removeBall();
    }

    public void setBall(int x, BallModel ball)
    {
        board.setBall(x, ball);
    }

    public boolean canReleaseBallFromStack()
    {
        return ballStack.isExistBall();
    }

    public boolean canPutBall(int x)
    {
        return ballStack.isExistBall() && board.canSetBall(x);
    }

    public int getDropPlace(int x)
    {
        return board.getDropPlace(x);
    }

    public boolean cancelAddingBall(int id)
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

    public boolean isAlive ()
    {
        return currentHp > 0 && board.isExistPlaceToPut();
    }

    public void outbreakMagic(int damage, BallInfoModel[] targets) {
        board.outbreakMagic(damage, targets);
    }

    public BallInfoModel[] getRemovedBallInfoModels()
    {
        return board.getRemovedBallInfoModels();
    }

    public BallInfoModel[] dropBalls()
    {
        return board.dropBalls();
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

    public ObstacleBallInfoModel[] getObstacleBallInfoModels(int damage, BallInfoModel[] targets) {
        return board.getObstacleBalls(damage, targets);
    }

}
