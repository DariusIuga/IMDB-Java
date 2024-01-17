package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Contributor.class, name = "Contributor"),

        @JsonSubTypes.Type(value = Admin.class, name = "Admin")}
)
public abstract class Staff<T extends Comparable<T>> extends User<T> implements StaffInterface, Subject{
    List<Request> requestsToSolve = new ArrayList<>();
    private SortedSet<CommonInterface> totalContribution = new TreeSet<>();
    private ArrayList<String> productionsContribution;
    private ArrayList<String> actorsContribution;


    public Staff(Information information, AccountType userType,
                 String username, int experience, List<String> notifications,
                 SortedSet<CommonInterface> commonInterfaces){
        super(information, userType,
                username, experience, notifications,
                commonInterfaces);
    }

    public Staff(){

    }

    // StaffInterface methods
    public void addProductionSystem(Production p){
        productionsContribution.add(p.getTitle());
        IMDB.productions.add(p);
    }

    public void addActorSystem(Actor a){
        IMDB.actors.add(a);
    }

    public void removeProductionSystem(String name){
        productionsContribution.removeIf(production -> production.equals(name));
        IMDB.productions.removeIf(production -> production.getTitle().equals(name));
    }

    public void removeActorSystem(String name){
        actorsContribution.removeIf(actor -> actor.equals(name));
        IMDB.actors.removeIf(actor -> actor.getName().equals(name));
    }

    public void updateProduction(Production p){
        productionsContribution.add(p.getTitle());
        IMDB.productions.add(p);
    }

    public void updateActor(Actor a){
        actorsContribution.add(a.getName());
        IMDB.actors.add(a);
    }

    public void resolveRequest(Request r){

    }

    public SortedSet<CommonInterface> getTotalContribution(){
        return totalContribution;
    }

    public void setTotalContribution(SortedSet<CommonInterface> totalContribution){
        this.totalContribution = totalContribution;
    }

    public void addToContribution(CommonInterface contribution){
        this.totalContribution.add(contribution);
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

    // Implement Subject methods
    @Override
    public void subscribe(Observer observer){
        // Implement subscription logic here
    }

    @Override
    public void unsubscribe(Observer observer){
        // Implement unsubscription logic here
    }

    @Override
    public void notifyObservers(String message){
        // Implement notification logic here
    }

    // Notifies users about resolved/rejected requests and reviewed productions
    public void notifyUsers(String message){
        this.update(message);
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
                ", totalContribution=" + totalContribution +
                "}\n";
    }
}
