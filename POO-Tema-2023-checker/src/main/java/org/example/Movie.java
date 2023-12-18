package org.example;

import java.util.ArrayList;
import java.util.Date;

public class Movie extends Production{

    private Date length;
    private short releaseYear;

    public Movie(String title, ArrayList<String> directorNames,
                 ArrayList<String> actorNames, ArrayList<Genre> genres,
                 ArrayList<Rating> ratings, String description,
                 double ranking, Date length, short releaseYear){
        super(title, directorNames, actorNames, genres, ratings, description,
                ranking);
        this.length = length;
        this.releaseYear = releaseYear;
    }

    public Date getLength(){
        return length;
    }

    public void setLength(Date length){
        this.length = length;
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
                "length=" + length +
                ", year=" + releaseYear +
                '}';
    }
}
