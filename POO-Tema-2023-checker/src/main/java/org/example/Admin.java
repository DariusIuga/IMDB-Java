package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;
import java.util.SortedSet;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
public class Admin<T extends Comparable<T>> extends Staff<T>{
    public Admin(Information information,
                 String username, int experience,
                 List<String> notifications,
                 SortedSet<Favorite> favorites){
        super(information, AccountType.ADMIN,
                username, experience, notifications,
                favorites);
    }

    public Admin(){

    }
}
