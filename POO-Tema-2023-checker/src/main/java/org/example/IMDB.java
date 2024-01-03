package org.example;

import java.util.ArrayList;

public class IMDB{
    // The working directory should be IMDB-Java
    static ArrayList<User<?>> accounts;
    static ArrayList<Actor> actors;
    static ArrayList<Production> productions;
    static ArrayList<Request> requests;
    private static IMDB instance;

    private IMDB(){
    }

    public static void deserializeJSON(){
        Deserializer deserializer = new Deserializer();
//        accounts = new ArrayList<>();
//        // Parse accounts
//        JsonNode jsonNode =
//                mapper.readTree(new File(pathToInputJSON +
//                        "/accounts.json"));
//
//        // Iterate over array entries and extract "userType" values
//        UserFactory factory = new UserFactory();
//        for (JsonNode entry : jsonNode){
//            String userType = entry.get("userType").asText();
//            // Construct each user with the coresponding type and add it
//            // to the list
//            User<String> user =
//                    factory.getUser(AccountType.valueOf(userType.toUpperCase()));
//            accounts.add(user);
//            System.out.println("User Type: " + userType);
//        }
        accounts = deserializer.deserializeUsers();
        for(User user: accounts){
            if(user instanceof Regular<?>){
                user.setUserType(AccountType.REGULAR);
            }
            else if(user instanceof Contributor<?>){
                user.setUserType(AccountType.CONTRIBUTOR);
            }
            else if(user instanceof Admin<?>){
                user.setUserType(AccountType.ADMIN);
            }
        }


        // Parse actors
        actors = deserializer.deserializeActors();

        // Parse productions
        productions = deserializer.deserializeProductions();

        // Parse requests
        requests = deserializer.deserializeRequests();
        for (Request request : requests){
            request.stringToLDT();
        }
    }

    public static IMDB getInstance(){
        if (instance == null){
            instance = new IMDB();
        }
        return instance;
    }

    public static void main(String[] args){
        IMDB imdb = IMDB.getInstance();
        imdb.run();
    }

    public void run(){
        deserializeJSON();
        System.out.println("ACCOUNTS:\n\n" + accounts);
        System.out.println("ACTORS:\n\n" + actors);
        System.out.println("PRODUCTIONS:\n\n" + productions);
        System.out.println("REQUESTS:\n\n" + requests);
    }
}
