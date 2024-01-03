package org.example;

public class Rating implements Subject{
    private String username;
    private byte rating;
    private String comment;

    public Rating(String username, byte rating, String comment){
        if(rating < 1 || rating > 10){
            System.err.println("Grades left by users should be between 1 and" +
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

    @Override
    public void subscribe(User user){

    }

    @Override
    public void unsubscribe(User user){

    }

    @Override
    public void Notify(){

    }

    @Override
    public String toString(){
        return "Rating{" +
                "username='" + username + '\'' +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                "}\n";
    }
}
