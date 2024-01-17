package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
public class Admin<T extends Comparable<T>> extends Staff<T>{
    public Admin(Information information,
                 String username, int experience,
                 List<String> notifications,
                 SortedSet<CommonInterface> commonInterfaces){
        super(information, AccountType.ADMIN,
                username, experience, notifications,
                commonInterfaces);
    }

    public Admin(){

    }

    public static class RequestsHolder{
        private static final List<Request> requests = new ArrayList<>();

        static void addRequest(Request r){
            requests.add(r);
        }

        static void deleteRequest(Request r){
            requests.remove(r);
        }
    }
}
