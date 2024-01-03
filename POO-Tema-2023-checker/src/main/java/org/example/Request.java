package org.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Request implements Subject{
    private RequestType type;
    private LocalDateTime date;
    private String movieTitle;
    private String actorName;

    private String description;
    private String username;
    private String to;

    // Wrapper for JSON
    private String createdDate;

    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    // Incomplet
//    public Request(RequestType typeEnum, String description, String username, String to){
//        this.typeEnum = typeEnum;
//        this.date = LocalDateTime.now();
//        this.description = description;
//        this.username = username;
//
//        switch (typeEnum){
//            case DELETE_ACCOUNT,OTHERS -> this.to = "ADMIN";
//            case ACTOR_ISSUE, MOVIE_ISSUE -> RequestsHolder.addRequest(this);
//        }
//    }

    public Request(){
    }

    public void stringToLDT(){
        if(createdDate != null){
            this.date = LocalDateTime.parse(createdDate,formatter);
        }
    }

    @Override
    public void subscribe(User user){

    }

    @Override
    public void unsubscribe(User user){

    }

    @Override
    public void Notify(){

    }

    public RequestType getType(){
        return type;
    }

    public void setType(RequestType type){
        this.type = type;
    }

    public LocalDateTime getDate(){
        return date;
    }

    public void setDate(LocalDateTime date){
        this.date = date;
    }

    public String getMovieTitle(){
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle){
        this.movieTitle = movieTitle;
    }

    public String getActorName(){
        return actorName;
    }

    public void setActorName(String actorName){
        this.actorName = actorName;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getTo(){
        return to;
    }

    public void setTo(String to){
        this.to = to;
    }

    public String getCreatedDate(){
        return createdDate;
    }

    public void setCreatedDate(String createdDate){
        this.createdDate = createdDate;
    }


    @Override
    public String toString(){
        return "Request{" +
                "type=" + type +
                ", date=" + date +
                ", movieTitle='" + movieTitle + '\'' +
                ", actorName='" + actorName + '\'' +
                ", description='" + description + '\'' +
                ", username='" + username + '\'' +
                ", to='" + to + '\'' +
                ", createdDate='" + createdDate + '\'' +
                "}\n";
    }
}
