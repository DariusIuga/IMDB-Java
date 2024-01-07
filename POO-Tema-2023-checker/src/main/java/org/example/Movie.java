package org.example;

import java.util.ArrayList;

public class Movie extends Production{

    private String duration;
    private short releaseYear;

    public Movie(String title, ArrayList<String> directorNames,
                 ArrayList<String> actorNames, ArrayList<Genre> genres,
                 ArrayList<Rating> ratings, String plot,
                 double averageRating, String duration, short releaseYear){
        super(title, directorNames, actorNames, genres, ratings, plot,
                averageRating);
        this.duration = duration;
        this.releaseYear = releaseYear;
    }

    public Movie(){
    }

    public String getDuration(){
        return duration;
    }

    public void setDuration(String duration){
        this.duration = duration;
    }

    public short getReleaseYear(){
        return releaseYear;
    }

    public void setReleaseYear(short releaseYear){
        this.releaseYear = releaseYear;
    }

    @Override
    public void displayInfo(){
        System.out.printf(this.toString());
    }


    @Override
    public String toString(){
        return "Movie{" +
                "title='" + super.getTitle() + '\'' +
                ", directors=" + super.getDirectors() +
                ", actors=" + super.getActors() +
                ", genres=" + super.getGenres() +
                ", ratings=" + super.getRatings() +
                ", plot='" + super.getPlot() + '\'' +
                ", averageRating=" + super.getAverageRating() +
                ", duration=" + duration +
                ", releaseYear=" + releaseYear +
                "}\n";
    }
}
