package com.example.administrador.photointentactivity;

/**
 * Created by Administrador on 24/10/2017.
 */

public class SpinnerModel {

    private  String Name="";
    private  int Level;
    private int Id;

    public SpinnerModel(int id, String name, int level){
        Id = id;
        Name = name;
        Level = level;
    }
    /*********** Set Methods ******************/
    public void setLevel(int level)
    {
        Level = level;
    }
    public void setId(int id)
    {
        Id = id;
    }

    public void setName(String name)
    {
        Name = name;
    }
    public String getName()
    {
        return Name;
    }



    public int getLevel()
    {
        return Level;
    }
    public int getId()
    {
        return Id;
    }

}