package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Deserializer{
    private static final String pathToInputJSON = "POO-Tema-2023-checker/src" +
            "/main" +
            "/resources/input";
    final static ObjectMapper mapper = new ObjectMapper();

    public ArrayList<User<?>> deserializeUsers(){
        try{
            return mapper.readValue(new File(pathToInputJSON + "/accounts" +
                                    ".json"),
                            new TypeReference<ArrayList<User<?>>>(){
                            });
        } catch (IOException e){
            e.printStackTrace();
            System.err.println("Error when parsing users json file!");
        }

        return null;
    }

    public ArrayList<Actor> deserializeActors(){
        try{
            return mapper.readValue(new File(pathToInputJSON + "/actors.json"),
                            new TypeReference<ArrayList<Actor>>(){
                            });
        } catch (IOException e){
            e.printStackTrace();
            System.err.println("Error when parsing actors json file!");
        }

        return null;
    }

    public ArrayList<Production> deserializeProductions(){
        try{
            return mapper.readValue(new File(pathToInputJSON + "/production" +
                            ".json"),
                    new TypeReference<ArrayList<Production>>(){
                    });
        } catch (IOException e){
            e.printStackTrace();
            System.err.println("Error when parsing productions json file!");
        }

        return null;
    }

    public ArrayList<Request> deserializeRequests(){
        try{
            return mapper.readValue(new File(pathToInputJSON +
                                    "/requests.json"),
                            new TypeReference<ArrayList<Request>>(){
                            });
        } catch (IOException e){
            e.printStackTrace();
            System.err.println("Error when parsing requests json file!");
        }

        return null;
    }

    public Deserializer(){
        mapper.findAndRegisterModules();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
}
