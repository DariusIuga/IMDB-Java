package org.example;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public abstract class Production implements Comparable<Production>{
    private String title;
    private ArrayList<String> directorNames;
    private ArrayList<String> actorNames;
    private ArrayList<Genre> genres;
    private ArrayList<Rating> ratings;
    private String description;
    private double ranking;

    public Production(String title, ArrayList<String> directorNames,
                      ArrayList<String> actorNames, ArrayList<Genre> genres,
                      ArrayList<Rating> ratings, String description,
                      double ranking){
        this.title = title;
        this.directorNames = directorNames;
        this.actorNames = actorNames;
        this.genres = genres;
        this.ratings = ratings;
        this.description = description;
        this.ranking = ranking;
    }


    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public ArrayList<String> getDirectorNames(){
        return directorNames;
    }

    public void setDirectorNames(ArrayList<String> directorNames){
        this.directorNames = directorNames;
    }

    public ArrayList<String> getActorNames(){
        return actorNames;
    }

    public void setActorNames(ArrayList<String> actorNames){
        this.actorNames = actorNames;
    }

    public ArrayList<Genre> getGenres(){
        return genres;
    }

    public void setGenres(ArrayList<Genre> genres){
        this.genres = genres;
    }

    public ArrayList<Rating> getRatings(){
        return ratings;
    }

    public void setRatings(ArrayList<Rating> ratings){
        this.ratings = ratings;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public double getRanking(){
        return ranking;
    }

    public void setRanking(double ranking){
        this.ranking = ranking;
    }

    public abstract void displayInfo();

    @Override
    public int compareTo(@NotNull Production o){
        return this.title.compareTo(o.title);
    }

    @Override
    public String toString(){
        return "Production{" +
                "title='" + title + '\'' +
                ", directorNames=" + directorNames +
                ", actorNames=" + actorNames +
                ", genres=" + genres +
                ", ratings=" + ratings +
                ", description='" + description + '\'' +
                ", ranking=" + ranking +
                '}';
    }
}
