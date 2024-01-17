package org.example;

import java.util.ArrayList;

public class Movie extends Production{

    private String duration;

    public Movie(String title, ArrayList<String> directorNames,
                 ArrayList<String> actorNames, ArrayList<Genre> genres,
                 ArrayList<Rating> ratings, String plot,
                 double averageRating, String duration, short releaseYear){
        super(title, directorNames, actorNames, genres, ratings, plot,
                averageRating, releaseYear);
        this.duration = duration;
    }

    public Movie(){
    }

    public String getDuration(){
        return duration;
    }

    public void setDuration(String duration){
        this.duration = duration;
    }

    @Override
    public void displayInfo(){
        System.out.printf(this.toString());
    }


    @Override
    public String toString(){
        return "Movie{" +
                "title='" + getTitle() +
                ", directors=" + getDirectors() +
                ", actors=" + getActors() +
                ", genres=" + getGenres() +
                ", ratings=" + getRatings() +
                ", plot='" + getPlot() +
                ", averageRating=" + getAverageRating() +
                ", duration=" + duration +
                ", releaseYear=" + getReleaseYear() +
                "}\n";
    }
}
