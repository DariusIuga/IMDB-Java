package org.example;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Actor implements CommonInterface{
    private String name;
    private ArrayList<Performance> performances;
    private String biography;

    public Actor(String name, ArrayList<Performance> performances, String biography){
        this.name = name;
        this.performances = performances;
        this.biography = biography;
    }

    public Actor(){

    }

    public String getName(){
        return name;
    }

    @Override
    public int compareTo(@NotNull CommonInterface o){
        return this.name.compareTo(o.getName());
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
        return this.name.compareTo(o.getName());
    }


}

class Performance{
    private String title;
    private String type;

    public Performance(String title, String type){
        this.title = title;
        this.type = type;
    }

    public Performance(){

    }

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
                "}\n";
    }
}
