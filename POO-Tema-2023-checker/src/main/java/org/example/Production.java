package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Movie.class, name = "Movie"),
        @JsonSubTypes.Type(value = Series.class, name = "Series")}
)
public abstract class Production implements CommonInterface,
        Comparable<CommonInterface>{
    private String title;
    private ArrayList<String> directors;
    private ArrayList<String> actors;
    @JsonDeserialize(using = GenresDeserializer.class)
    private ArrayList<Genre> genres;
    private ArrayList<Rating> ratings;
    private String plot;
    private double averageRating;
    private short releaseYear;


    public Production(String title, ArrayList<String> directors,
                      ArrayList<String> actors, ArrayList<Genre> genres,
                      ArrayList<Rating> ratings, String plot,
                      double averageRating, short releaseYear){
        this.title = title;
        this.directors = directors;
        this.actors = actors;
        this.genres = genres;
        this.ratings = ratings;
        this.plot = plot;
        this.averageRating = averageRating;
        this.releaseYear = releaseYear;
    }

    public Production(){

    }


    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public ArrayList<String> getDirectors(){
        return directors;
    }
    public String getDirector(int i){
        return directors.get(i);
    }


    public void setDirectors(ArrayList<String> directors){
        this.directors = directors;
    }


    public ArrayList<String> getActors(){
        return actors;
    }
    public String getActor(int i){
        return actors.get(i);
    }


    public void setActors(ArrayList<String> actors){
        this.actors = actors;
    }

    public ArrayList<Genre> getGenres(){
        return genres;
    }

    public void setGenres(ArrayList<Genre> genres){
        this.genres = genres;
    }

    public void addGenre(Genre genre){
        if(!this.genres.contains(genre)){
            this.genres.add(genre);
        }
    }

    public ArrayList<Rating> getRatings(){
        return ratings;
    }

    public void setRatings(ArrayList<Rating> ratings){
        this.ratings = ratings;
    }

    public void addRating(Rating rating){
        this.ratings.add(rating);
    }

    public String getPlot(){
        return plot;
    }

    public void setPlot(String plot){
        this.plot = plot;
    }

    public double getAverageRating(){
        // Calculate new ranking on demand, instead of computing it when
        // adding a new grade
        int ratingsSum = 0;
        for (Rating r : ratings){
            ratingsSum += r.getRating();
        }

        // The final ranking is the average of all grades submitted by all users
        averageRating = (double) ratingsSum / ratings.size();

        return averageRating;
    }

    public abstract void displayInfo();

    @Override
    public String getName(){
        return title;
    }
    public short getReleaseYear(){
        return releaseYear;
    }

    public void setReleaseYear(short releaseYear){
        if(releaseYear > 1920 || releaseYear <= 2024){
            this.releaseYear = releaseYear;
        }
        else{
            System.err.println("Invalid release year!");
        }
    }


    @Override
    public int compareTo(@NotNull CommonInterface o){
        return this.title.compareTo(o.getName());

    }

    @Override
    public String toString(){
        return "Production{" +
                "title='" + title + '\'' +
                ", directors=" + directors +
                ", actors=" + actors +
                ", genres=" + genres +
                ", ratings=" + ratings +
                ", plot='" + plot + '\'' +
                ", averageRating=" + averageRating +
                ", releaseYear=" + releaseYear +
                "}\n";
    }

}
