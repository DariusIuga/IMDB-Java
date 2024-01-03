package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;
import java.util.SortedSet;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
public class Contributor<T extends Comparable<T>> extends Staff<T> implements RequestsManager{

    public Contributor(Information information,
                       String username, int experience,
                       List<String> notifications,
                       SortedSet<T> favorites){
        super(information, AccountType.CONTRIBUTOR,
                username, experience, notifications,
                favorites);
    }

    public Contributor(){

    }

    @Override
    public void createRequest(Request r){
        this.requestsToSolve.add(r);
    }

    @Override
    public void removeRequest(Request r){
        this.requestsToSolve.remove(r);
    }
}
