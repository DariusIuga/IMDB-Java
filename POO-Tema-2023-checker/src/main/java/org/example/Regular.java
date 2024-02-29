package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
public class Regular<T extends Comparable<T>> extends User<T> implements RequestsManager{

    public Regular(Information information,
                   String username, int experience,
                   List<String> notifications,
                   SortedSet<CommonInterface> commonInterfaces){
        super(information, AccountType.REGULAR,
                username, experience, notifications,
                commonInterfaces);
    }

    private ArrayList<Rating> myRatings = null;

    public Regular(){

    }

    @Override
    public void createRequest(Request r){

    }

    @Override
    public void removeRequest(Request r){
    }

    public void findRatings(){
        for(Production production: IMDB.productions){
            for(Rating rating: production.getRatings()){
                if(rating.getUsername().equals(getUsername())){
                    this.myRatings.add(rating);
                }
            }
        }
    }

    public ArrayList<Rating> getMyRatings(){
        return myRatings;
    }

    public void setMyRatings(ArrayList<Rating> myRatings){
        this.myRatings = myRatings;
    }

    public void addRating(Production production, byte grade, String comment){
        production.addRating(new Rating(this.getUsername(), grade, comment));
        setStrategy(new RatingGain());
    }

    public void addRating(Production production,Rating rating){
        production.addRating(rating);
        setStrategy(new RatingGain());
    }

    @Override
    public String toString(){
        return "Regular{" +
                "information=" + getInformation() +
                ", userType=" + getUserType() +
                ", username='" + getUsername() + '\'' +
                ", experience=" + getExperience() +
                ", notifications=" + getNotifications() +
                ", favorites=" + getFavorites() +
                ", favoriteProductions=" + favoriteProductions +
                ", favoriteActors=" + favoriteActors +
                ", myRatings=" + myRatings +
                '}';
    }
}
