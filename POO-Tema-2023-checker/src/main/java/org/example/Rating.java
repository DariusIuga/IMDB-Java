package org.example;

import java.util.ArrayList;
import java.util.List;

public class Rating implements Subject{
    private String username;
    private byte rating;
    private String comment;
    private List<Observer> observers = new ArrayList<>();

    public Rating(String username, byte rating, String comment){
        if(rating < 1 || rating > 10){
            System.err.println("Ratings left by users should be between 1 and" +
                    " 10!");
        }
        this.username = username;
        this.rating = rating;
        this.comment = comment;
    }

    public Rating(){
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public byte getRating(){
        return rating;
    }

    public void setRating(byte rating){
        if(rating < 1 || rating > 10){
            System.err.println("Grades left by users should be between 1 and" +
                    " 10!");
        }
        this.rating = rating;
    }

    public String getComment(){
        return comment;
    }

    public void setComment(String comment){
        this.comment = comment;
    }

    // Implement Subject methods
    @Override
    public void subscribe(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void unsubscribe(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

    @Override
    public String toString(){
        return "Rating{" +
                "username='" + username + '\'' +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                ", observers=" + observers +
                '}';
    }
}
