package org.example;

import java.util.ArrayList;
import java.util.Scanner;

public class IMDB{
    private static final Scanner scanner = new Scanner(System.in);
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
        accounts = deserializer.deserializeUsers();
        for (User<?> user : accounts){
            if (user instanceof Regular<?>){
                user.setUserType(AccountType.REGULAR);
            } else if (user instanceof Contributor<?>){
                user.setUserType(AccountType.CONTRIBUTOR);
            } else if (user instanceof Admin<?>){
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

        for (User<?> user : accounts){
            if(user.favoriteProductions != null){
                for (String productionString : user.favoriteProductions){
                    for (Production production : productions){
                        if (production.getTitle().equals(productionString)){
                            user.addToFavorites(production);
                        }
                    }
                }
            }
            if(user.favoriteActors != null){
                for (String actorString : user.favoriteActors){
                    for (Actor actor : actors){
                        if (actor.getName().equals(actorString)){
                            user.addToFavorites(actor);
                        }
                    }
                }
            }
        }
    }

    public static <T extends User<?>> T login(){
        System.out.println("Welcome back! Enter your credentials to log " +
                "in!\n");

        User<?> activeUser = null;

        boolean emailFound = false;
        do{
            System.out.print("\temail: ");
            String email = scanner.nextLine().trim();
            for (User<?> account : accounts){
                if (account.getInformation().getCredentials().getEmail().equals(email)){
                    activeUser = account;
                    emailFound = true;
                }
            }
            if (!emailFound){
                System.out.println("No account with this email was found! Try" +
                        " " +
                        "again.");
            }
        }
        while (!emailFound);

        byte remainingAttempts = 5;
        while (true){
            System.out.print("\tpassword: ");
            String password = scanner.nextLine().trim();
            if (activeUser.getInformation().getCredentials().getPassword().equals(password)){
                break;
            } else{
                remainingAttempts--;
                System.out.println("Wrong password for this account! " + remainingAttempts + " attempts left.");
            }
            if (remainingAttempts == 0){
                remainingAttempts = 5;
                System.out.println("Please wait 15 seconds before trying " +
                        "again");
                try{
                    Thread.sleep(15000);
                } catch (InterruptedException e){
                    System.out.println("Stopped current thread");
                }
            }
        }

        System.out.println("Welcome back user " + activeUser.getUsername() +
                "!");
        System.out.println("Username: " + activeUser.getUsername());
        System.out.println("Account type: " + activeUser.getUserType());
        System.out.println("User experience: " + activeUser.getExperience());

        @SuppressWarnings("unchecked")
        T typedUser = (T) activeUser;
        return typedUser;
    }

    public static void logout(){
        System.out.println("\n You have logged out! Do you want to log in " +
                "again? (yes/no)");
        String choice = scanner.nextLine().trim();
        if (choice.equals("yes")){
            User<?> activeUser = login();
            showOptions(activeUser);
        } else{
            // Closes the current process
            System.exit(0);
        }
    }

    public static void showOptions(User<?> activeUser){
        AccountType type = activeUser.getUserType();

        while (true){
            System.out.println("\nChoose action:");
            System.out.println("\t1) View productions details");
            System.out.println("\t2) View actors details");
            System.out.println("\t3) View notifications");
            System.out.println("\t4) Search for actor/movie/series");
            System.out.println("\t5) Add/Delete actor/movie/series to/from " +
                    "favorites");
            switch (type){
                case REGULAR -> {
                    System.out.println("\t6) Add/Delete request");
                    System.out.println("\t7) Add/Delete review for " +
                            "movies/series");
                    System.out.println("\t8) Log Out");
                }
                case CONTRIBUTOR -> {
                    System.out.println("\t6) Add/Delete request");
                    System.out.println("\t7) Add/Delete actor/movie/series " +
                            "to/from system");
                    System.out.println("\t8) View and solve requests");
                    System.out.println("\t9) Update movie/actor details");
                    System.out.println("\t10) Log Out");
//                    // Test
//                    System.out.println(activeUser.getActorsContribution());
                }
                case ADMIN -> {
                    System.out.println("\t6) Add/Delete actor/movie/series " +
                            "to/from system");
                    System.out.println("\t7) View and solve requests");
                    System.out.println("\t8) Update movie/actor details");
                    System.out.println("\t9) Add/Delete user");
                    System.out.println("\t10) Log Out");
                }
                default -> System.err.println("Invalid user type when" +
                        " showing " +
                        "user options!");
            }

            // The user enters a number corresponding to his/her choice
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice){
                case 1 -> System.out.println(productions);
                case 2 -> System.out.println(actors);
                case 3 -> System.out.println(activeUser.getNotifications());
                case 4 -> {
                    System.out.println("What do you want to search " +
                            "for? (actor/production)");
                    switch (scanner.nextLine().trim()){
                        case "actor" -> {
                            System.out.println("Enter the name of the" +
                                    " actor you want to find: ");
                            String name = scanner.nextLine().trim();
                            for (Actor actor : actors){
                                if (actor.getName().equals(name)){
                                    System.out.println(actor);
                                }
                            }
                        }
                        case "production" -> {
                            System.out.println("Enter the name of the" +
                                    " movie/series you want to find: ");
                            String name = scanner.nextLine().trim();
                            for (Production production : productions){
                                if (production.getTitle().equals(name)){
                                    System.out.println(production);
                                }
                            }
                        }
                        default -> System.out.println("Enter 'actor' or " +
                                "'production' please");
                    }
                }
                case 5 -> {
                    manageFavorites(activeUser);
                }
                default -> advancedOptions(choice, activeUser);
            }
        }

    }

    public static void manageFavorites(User<?> activeUser){
        ArrayList<String> favoriteActors = activeUser.getFavoriteActors();
        ArrayList<String> favoriteProductions =
                activeUser.getFavoriteProductions();
        System.out.println("These are your current favorite actors:\n" + favoriteActors);
        System.out.println("These are your current favorite productions:\n" + favoriteProductions);
        System.out.println("Do you want to add or delete " +
                "something " +
                "from you favorites list? (add/delete)");

        switch (scanner.nextLine().trim()){
            case "add" -> {
                System.out.println("What do you want to add? " +
                        "(actor/production)");
                switch (scanner.nextLine().trim()){
                    case "actor" -> {
                        System.out.println("Enter the name of the" +
                                " actor you want to add to your " +
                                "favorites list: ");
                        String name = scanner.nextLine().trim();
                        for (Actor actor : actors){
                            if (actor.getName().equals(name)){
                                activeUser.favoriteActors.add(name);
                                activeUser.addToFavorites(actor);
                            }
                        }
                    }
                    case "production" -> {
                        System.out.println("Enter the name of the" +
                                " production you want to add to your " +
                                "favorites list: ");
                        String name = scanner.nextLine().trim();
                        for (Production production : productions){
                            if (production.getName().equals(name)){
                                activeUser.favoriteProductions.add(name);
                                activeUser.addToFavorites(production);
                            }
                        }
                    }
                    default -> System.out.println("Enter 'actor' or " +
                            "'production' please");

                }
            }

            case "delete" -> {
                System.out.println("What do you want to delete? " +
                        "(actor/production)");
                switch (scanner.nextLine().trim()){
                    case "actor" -> {
                        System.out.println("Enter the name of the" +
                                " actor you want to delete from " +
                                "your " +
                                "favorites list: ");
                        String name = scanner.nextLine().trim();
                        for (Actor actor : actors){
                            if (actor.getName().equals(name)){
                                activeUser.favoriteActors.remove(name);
                                activeUser.deleteFromFavorites(actor);
                            }
                        }
                    }
                    case "production" -> {
                        System.out.println("Enter the name of the" +
                                " movie/series you want to delete" +
                                " " +
                                "from your favorites list: ");
                        String name = scanner.nextLine().trim();
                        for (Production production : productions){
                            if (production.getTitle().equals(name)){
                                activeUser.favoriteProductions.remove(name);
                                activeUser.deleteFromFavorites(production);
                            }
                        }
                    }
                    default -> System.out.println("Enter 'actor' or " +
                            "'production' please");

                }
            }
            default -> System.out.println("Enter 'add' or " +
                    "'delete' please");
        }
    }

    public static void advancedOptions(int choice,User<?> activeUser){
        AccountType type = activeUser.getUserType();
        switch (type){
            case REGULAR -> {
                switch (choice){
                    case 6 -> manageRequests();
                    case 7 -> manageReviews();
                    case 8 -> {
                        logout();
                    }
                    default -> System.out.println("Invalid input when " +
                            "selectng options: please enter an integer " +
                            "between 1 and 8!");
                }
            }
            case CONTRIBUTOR -> {
                switch (choice){
                    case 6 -> manageRequests();
                    case 7 -> manageProductionsAndActors();
                    case 8 -> solveRequests();
                    case 9 -> updateInfo();
                    case 10 -> {
                        logout();
                    }
                    default -> System.out.println("Invalid input when " +
                            "selectng options: please enter an integer " +
                            "between 1 and 11!");
                }
            }
            case ADMIN -> {
                switch (choice){
                    case 6 -> manageProductionsAndActors();
                    case 7 -> solveRequests();
                    case 8 -> updateInfo();
                    case 9 -> manageUsers();
                    case 10 -> {
                        logout();
                    }
                    default -> System.out.println("Invalid input when " +
                            "selecting options: please enter an integer " +
                            "between 1 and 11!");
                }
            }
            default -> {
                System.err.println("Error: Invalid user type");
            }
        }
    }

    public static void manageRequests(){
        System.out.println("TODO");
    }

    public static void manageProductionsAndActors(){
        System.out.println("What do you want to change (productions/actors)");
        switch (scanner.nextLine().trim()){
            case "productions" -> {
                System.out.println("These are all the current registered " +
                                "productions:\n" + productions);
                System.out.println("Do you want to add or delete a " +
                        "production? (add/delete)");
                switch (scanner.nextLine().trim()){
                    case "add" ->{
                        System.out.println("TODO: Add production to system");
                    }
                    case "delete" ->{
                        System.out.println("Enter the name of the " +
                                "movie/series that you want to delete: ");
                        String name = scanner.nextLine().trim();
                        productions.removeIf(production -> production.getTitle().equals(name));
                    }

                }
            }
            case "actors" -> {
                System.out.println("These are all the current registered " +
                        "actors:\n" + actors);
                System.out.println("Do you want to add or delete am " +
                        "actor? (add/delete)");
                switch (scanner.nextLine().trim()){
                    case "add" ->{
                        System.out.println("TODO: Add actor to system");
                    }
                    case "delete" ->{
                        System.out.println("Enter the name of the " +
                                "actor that you want to delete: ");
                        String name = scanner.nextLine().trim();
                        actors.removeIf(actor -> actor.getName().equals(name));
                    }

                }
            }
            default -> System.err.println("Please enter a valid option! " +
                    "(productions/actors)");
        }
    }

    public static void solveRequests(){
        System.out.println("TODO");
    }

    public static void updateInfo(){
        System.out.println("TODO");
    }

    public static void manageReviews(){
        System.out.println("TODO");
    }

    public static void manageUsers(){
        System.out.println("TODO");
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
        // Loads data from the json files into the 4 ArrayLists
        deserializeJSON();

        // Select what mode the app will be run in (terminal or gui)
        while (true){
            System.out.println("How do you want to run this app? " +
                    "(terminal/gui)");
            String appMode = scanner.nextLine().trim();

            if (appMode.equals("terminal")){
                User<?> activeUser = login();
                // Determine the specific type of the user
                if (activeUser instanceof Regular) {
                    showOptions((Regular<?>) activeUser);
                } else if (activeUser instanceof Contributor) {
                    showOptions((Contributor<?>) activeUser);
                } else if (activeUser instanceof Admin) {
                    showOptions((Admin<?>) activeUser);
                } else {
                    System.err.println("Invalid user type");
                }
                break;
            } else if (appMode.equals("gui")){
                System.out.println("Sorry, I haven't implemented a user " +
                        "interface" +
                        " " +
                        "yet...");
                break;
            }
        }

//        System.out.println("ACCOUNTS:\n\n" + accounts);
//        System.out.println("ACTORS:\n\n" + actors);
//        System.out.println("PRODUCTIONS:\n\n" + productions);
//        System.out.println("REQUESTS:\n\n" + requests);
    }
}
