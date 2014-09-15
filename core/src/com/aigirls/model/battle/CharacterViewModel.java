package com.aigirls.model.battle;

public class CharacterViewModel {
    private String name;
    private String imageName;

    public CharacterViewModel(String name, String imageName)
    {
        this.name = name;
        this.imageName = imageName;
    }

    public String getName()
    {
        return name;
    }

    public String getImageName()
    {
        return imageName;
    }

}
