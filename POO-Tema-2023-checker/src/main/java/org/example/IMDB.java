package org.example;
import java.util.ArrayList;

public class IMDB{
    ArrayList<?> users;
    ArrayList<Actor> actors;
    ArrayList<Request> requests;
    ArrayList<?> movies;

    private static IMDB instance;

    private IMDB(){

    }

    public void run(){

    }

    public static IMDB getInstance(){
        if(instance == null){
            instance = new IMDB();
        }
        return instance;
    }

}
