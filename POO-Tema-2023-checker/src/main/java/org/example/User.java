package org.example;

import java.util.List;
import java.util.SortedSet;

public abstract class User<T extends Comparable<T>>{
    private Information info;
    private AccountType type;
    private String username;
    private int experience;
    private List<String> notifications;
    private SortedSet<T> favorites;

    public User(Information info, AccountType type,
                List<String> notifications, SortedSet<T> favorites){
        this.info = info;
        this.type = type;
        this.notifications = notifications;
        this.favorites = favorites;

        if(type == AccountType.ADMIN){
            this.experience = Integer.MAX_VALUE;
        }
        else{
            this.experience = 0;
        }

        // TODO: Generarea unui username si parola unice
    }


    public Information getInfo(){
        return info;
    }

    public void setInfo(Information info){
        this.info = info;
    }

    public AccountType getType(){
        return type;
    }

    public void setType(AccountType type){
        this.type = type;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public int getExperience(){
        return experience;
    }

    public void setExperience(int experience){
        this.experience = experience;
    }

    public List<String> getNotifications(){
        return notifications;
    }

    public void setNotifications(List<String> notifications){
        this.notifications = notifications;
    }

    public SortedSet<T> getFavorites(){
        return favorites;
    }

    public void setFavorites(SortedSet<T> favorites){
        this.favorites = favorites;
    }

    public void addToFavorites(T obj){
        favorites.add(obj);
    }

    public void deleteFromFavorites(T obj){
        favorites.remove(obj);
    }

    public void logOut(){
        // TODO: functionalitate pentru delogarea utilizatorului curent
    }
}
