package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Contributor.class, name = "Contributor"),

        @JsonSubTypes.Type(value = Admin.class, name = "Admin")}
)
public abstract class Staff<T extends Comparable<T>> extends User<T> implements StaffInterface{
    List<Request> requestsToSolve;
    private SortedSet<T> addedToDatabase;
    private ArrayList<String> productionsContribution;
    private ArrayList<String> actorsContribution;


    public Staff(Information information, AccountType userType,
                 String username, int experience, List<String> notifications,
                 SortedSet<Favorite> favorites){
        super(information, userType,
                username, experience, notifications,
                favorites);
    }

    public Staff(){

    }

    public void addObjectToDB(T obj){

        if (obj instanceof Actor){
            addActorSystem((Actor) obj);
        } else if (obj instanceof Production){
            addProductionSystem((Production) obj);
        } else{
            System.err.println("Invalid object type to add to the system!");
        }
    }

    public void deleteObjectFromDB(T obj){
        if (obj instanceof Actor){
            removeActorSystem(obj.toString());
        } else if (obj instanceof Production){
            removeProductionSystem(obj.toString());
        } else{
            System.err.println("Invalid object type to remove from the " +
                    "system!");
        }
    }

    // StaffInterface methods
    public void addProductionSystem(Production p){
        addedToDatabase.add((T) p);
    }

    public void addActorSystem(Actor a){
        addedToDatabase.add((T) a);
    }

    public void removeProductionSystem(String name){

    }

    public void removeActorSystem(String name){

    }

    public void updateProduction(Production p){

    }

    public void updateActor(Actor a){

    }

    public void resolveRequest(Request r){

    }


    public SortedSet<T> getAddedToDatabase(){
        return addedToDatabase;
    }

    public void setAddedToDatabase(SortedSet<T> addedToDatabase){
        this.addedToDatabase = addedToDatabase;
    }

    public List<Request> getRequestsToSolve(){
        return requestsToSolve;
    }

    public void setRequestsToSolve(List<Request> requestsToSolve){
        this.requestsToSolve = requestsToSolve;
    }


    public ArrayList<String> getActorsContribution(){
        return actorsContribution;
    }

    public void setActorsContribution(ArrayList<String> actorsContribution){
        this.actorsContribution = actorsContribution;
    }

    public ArrayList<String> getProductionsContribution(){
        return productionsContribution;
    }

    public void setProductionsContribution(ArrayList<String> productionsContribution){
        this.productionsContribution = productionsContribution;
    }

    @Override
    public String toString(){
        return "User{" +
                "information=" + super.getInformation() +
                ", userType=" + super.getUserType() +
                ", username='" + super.getUsername() + '\'' +
                ", experience=" + super.getExperience() +
                ", notifications=" + super.getNotifications() +
                ", favorites=" + super.getFavorites() +
                ", favoriteProductions=" + super.getFavoriteProductions() +
                ", favoriteActors=" + super.getFavoriteActors() +
                ", productionsContribution=" + productionsContribution +
                ", actorsContribution=" + actorsContribution +
                "}\n";
    }
}
