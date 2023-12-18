package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Series extends Production{

    private short releaseYear;
    private int numberOfSeasons;
    private Map<String, List<Episode>> seasonsMap;

    public Series(String title, ArrayList<String> directorNames,
                  ArrayList<String> actorNames, ArrayList<Genre> genres,
                  ArrayList<Rating> ratings, String description,
                  double ranking, short releaseYear, int numberOfSeasons,
                  Map<String, List<Episode>> seasonsMap){
        super(title, directorNames, actorNames, genres, ratings, description,
                ranking);
        this.releaseYear = releaseYear;
        this.numberOfSeasons = numberOfSeasons;
        this.seasonsMap = seasonsMap;
    }

    @Override
    public void displayInfo(){
        System.out.printf(this.toString());
    }

    public short getReleaseYear(){
        return releaseYear;
    }

    public void setReleaseYear(short releaseYear){
        this.releaseYear = releaseYear;
    }

    public int getNumberOfSeasons(){
        return numberOfSeasons;
    }

    public void setNumberOfSeasons(int numberOfSeasons){
        this.numberOfSeasons = numberOfSeasons;
    }

    public Map<String, List<Episode>> getSeasonsMap(){
        return seasonsMap;
    }

    public void setSeasonsMap(Map<String, List<Episode>> seasonsMap){
        this.seasonsMap = seasonsMap;
    }
}
