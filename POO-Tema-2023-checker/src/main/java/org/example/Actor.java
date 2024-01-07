package org.example;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Actor implements Favorite{
    private String name;
    private ArrayList<Performance> performances;
    private String biography;

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public ArrayList<Performance> getPerformances(){
        return performances;
    }

    public void setPerformances(ArrayList<Performance> performances){
        this.performances = performances;
    }

    public String getBiography(){
        return biography;
    }

    public void setBiography(String biography){
        this.biography = biography;
    }

    @Override
    public String toString(){
        return "Actor{" +
                "name='" + name + '\'' +
                ", performances=" + performances +
                ", biography='" + biography + '\'' +
                "}\n";
    }

    public int compareTo(@NotNull Actor o){
        return 0;
    }


}

class Performance{
    private String title;
    private String type;

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getType(){
        return type;
    }

    public void setType(String type){
        this.type = type;
    }

    @Override
    public String toString(){
        return "Performance{" +
                "title='" + title + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
