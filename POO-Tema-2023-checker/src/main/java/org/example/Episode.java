package org.example;

import java.util.Date;

public class Episode{
    private String name;
    private Date length;

    public Episode(String name, Date length){
        this.name = name;
        this.length = length;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Date getLength(){
        return length;
    }

    public void setLength(Date length){
        this.length = length;
    }


}
