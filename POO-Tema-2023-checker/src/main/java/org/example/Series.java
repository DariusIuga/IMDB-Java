package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Series extends Production{

    private int numSeasons;
    private Map<String, List<Episode>> seasons;

    public Series(String title, ArrayList<String> directorNames,
                  ArrayList<String> actorNames, ArrayList<Genre> genres,
                  ArrayList<Rating> ratings, String description,
                  double ranking, short releaseYear, int numSeasons,
                  Map<String, List<Episode>> seasons){
        super(title, directorNames, actorNames, genres, ratings, description,
                ranking, releaseYear);
        this.numSeasons = numSeasons;
        this.seasons = seasons;
    }

    public Series(){

    }

    @Override
    public void displayInfo(){
        System.out.printf(this.toString());
    }

    public int getNumSeasons(){
        return numSeasons;
    }

    public void setNumSeasons(int numSeasons){
        this.numSeasons = numSeasons;
    }

    public Map<String, List<Episode>> getSeasons(){
        return seasons;
    }

    public void setSeasons(Map<String, List<Episode>> seasons){
        this.seasons = seasons;
    }

    @Override
    public String toString(){
        return "Series{" +
                "title='" + getTitle() + '\'' +
                ", directors=" + getDirectors() +
                ", actors=" + getActors() +
                ", genres=" + getGenres() +
                ", ratings=" + getRatings() +
                ", plot='" + getPlot() + '\'' +
                ", averageRating=" + getAverageRating() +
                ", releaseYear=" + getReleaseYear() +
                ", numberOfSeasons=" + numSeasons +
                ", seasonsMap=" + seasons +
                "}\n";
    }
}
