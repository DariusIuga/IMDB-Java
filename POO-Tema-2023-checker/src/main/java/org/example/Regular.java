package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;
import java.util.SortedSet;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
public class Regular<T extends Comparable<T>> extends User<T> implements RequestsManager{

    public Regular(Information information,
                   String username, int experience,
                   List<String> notifications,
                   SortedSet<Favorite> favorites){
        super(information, AccountType.REGULAR,
                username, experience, notifications,
                favorites);
    }

    public Regular(){

    }

    @Override
    public void createRequest(Request r){

    }

    @Override
    public void removeRequest(Request r){
    }

    public void addRating(Production production, byte grade, String comment){
        production.addRating(new Rating(this.getUsername(), grade, comment));
    }


}
