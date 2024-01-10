package org.example;

import java.util.Date;

public class Episode{
    private String episodeName;
    private String duration;

    public Episode(String episodeName, String duration){
        this.episodeName = episodeName;
        this.duration = duration;
    }

    public Episode(){

    }

    public String getEpisodeName(){
        return episodeName;
    }

    public void setEpisodeName(String episodeName){
        this.episodeName = episodeName;
    }

    public String getDuration(){
        return duration;
    }

    public void setDuration(String duration){
        this.duration = duration;
    }


    @Override
    public String toString(){
        return "Episode{" +
                "episodeName='" + episodeName + '\'' +
                ", duration='" + duration + '\'' +
                "}\n";
    }
}
