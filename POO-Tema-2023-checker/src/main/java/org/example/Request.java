package org.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Request{
    private RequestType type;
    private LocalDateTime creationDate;
    private String title;
    private String description;
    private String authorUsername;
    private String adminUsername;


    public Request(RequestType type, String title, String description, String authorUsername, String adminUsername){
        this.type = type;
        this.creationDate = LocalDateTime.now();
        this.title = title;
        this.description = description;
        this.authorUsername = authorUsername;

        switch (type){
            case DELETE_ACCOUNT,OTHERS -> this.adminUsername = "ADMIN";
            case ACTOR_ISSUE, MOVIE_ISSUE -> RequestsHolder.addRequest(this);
        }
    }
}
