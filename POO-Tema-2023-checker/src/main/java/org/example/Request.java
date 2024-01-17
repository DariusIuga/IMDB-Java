package org.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Request implements Subject{
    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private RequestType type;
    private LocalDateTime date;
    private String movieTitle;
    private String actorName;
    private String description;
    private String username;
    private String to;
    // Wrapper for JSON
    private String createdDate;
    private final List<Observer> observers = new ArrayList<>();

    public Request(){
        date = LocalDateTime.now();
        createdDate = date.format(formatter);
    }

    public Request(RequestType type, String description){
        super();
        this.type = type;
        this.description = description;
        if(type.equals(RequestType.DELETE_ACCOUNT) || type.equals(RequestType.OTHERS)){
            this.to = "ADMIN";
        }
    }

    public Request(RequestType type, String description, String name){
        this(type,description);
        if(type.equals(RequestType.ACTOR_ISSUE)){
            this.actorName = name;

            // Get the name of the Staff member that added this actor
            for(User<?> user: IMDB.accounts){
                if(user.getUserType().equals(AccountType.CONTRIBUTOR) || user.getUserType().equals(
                        AccountType.ADMIN)){
                    Staff<?> staff = (Staff<?>) user;
                    ArrayList<String> actorsContribution =
                            staff.getActorsContribution();
                    if(actorsContribution != null){
                        for(String actor: actorsContribution){
                            if (name.equals(actor)){
                                this.to = user.getUsername();
                                break;
                            }
                        }
                    }
                }
            }
        } else if (type.equals(RequestType.MOVIE_ISSUE)){
            this.movieTitle = name;

            // Get the name of the Staff member that added this movie
            for(User<?> user: IMDB.accounts){
                if(user.getUserType().equals(AccountType.CONTRIBUTOR) || user.getUserType().equals(
                        AccountType.ADMIN)){
                    Staff<?> staff = (Staff<?>) user;
                    ArrayList<String>  productionsContribution =
                            staff.getProductionsContribution();
                    if(productionsContribution != null){
                        for(String movie: productionsContribution){
                            if (name.equals(movie)){
                                this.to = user.getUsername();
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    public void stringToLDT(){
        if (createdDate != null){
            this.date = LocalDateTime.parse(createdDate, formatter);
        }
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

    // Implement Subject methods
    @Override
    public void subscribe(Observer observer){
        observers.add(observer);
    }

    @Override
    public void unsubscribe(Observer observer){
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String message){
        for (Observer observer : observers){
            observer.update(message);
        }
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
