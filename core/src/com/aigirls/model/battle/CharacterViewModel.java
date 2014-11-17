package com.aigirls.model.battle;

public class CharacterViewModel {
    /** 描画に必要なステータスをここに持ってくる */
    private String name;
    private String imageName;
    private int maxHp;

    public CharacterViewModel(String name, String imageName, int maxHp)
    {
        this.name = name;
        this.imageName = imageName;
        this.maxHp = maxHp;
    }

    public String getName()
    {
        return name;
    }

    public String getImageName()
    {
        return imageName;
    }

    public int getMaxHp()
    {
        return maxHp;
    }

}
