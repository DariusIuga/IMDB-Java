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
    private static User<?> activeUser;
    private static String appMode;

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
    }

    public static User<?> login(){
        System.out.println("Welcome back! Enter your credentials to log " +
                "in!\n");

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

        return activeUser;
    }

    public static void logout(){
        System.out.println("\n You have logged out! Do you want to log in " +
                "again? (yes/no)");
        String choice = scanner.nextLine().trim();
        if (choice.equals("yes")){
            activeUser = login();
            showOptions();
        } else{
            // Closes the current process
            System.exit(0);
        }
    }

    public static void showOptions(){
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
                    System.out.println("\t9) Update movie details");
                    System.out.println("\t10) Update actor details");
                    System.out.println("\t11) Log Out");
                }
                case ADMIN -> {
                    System.out.println("\t6) Add/Delete actor/movie/series " +
                            "to/from system");
                    System.out.println("\t7) View and solve requests");
                    System.out.println("\t8) Update movie details");
                    System.out.println("\t9) Update actor details");
                    System.out.println("\t10) Add/Delete user");
                    System.out.println("\t11) Log Out");
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
                    manageFavorites();
                }
                default -> advancedOptions(choice);
            }
        }

    }

    public static void manageFavorites(){
        System.out.println("Do you want to add or delete " +
                "something " +
                "from you favorites list? (add/delete)");
        switch (scanner.nextLine().trim()){
            case "add" -> {
                System.out.println("What do you want to add? " +
                        "(actor/production");
                switch (scanner.nextLine().trim()){
                    case "actor" -> {
                        System.out.println("Enter the name of the" +
                                " actor you want to add to your" +
                                "favorites list: ");
                        String name = scanner.nextLine().trim();
                        for (Actor actor : actors){
                            if (actor.getName().equals(name)){
                                activeUser.addToFavorites(actor);
                            }
                        }
                    }
                    case "production" -> {
                        System.out.println("Enter the name of the" +
                                " movie/series you want to add " +
                                "to your favorites list: ");
                        String name = scanner.nextLine().trim();
                        for (Production production : productions){
                            if (production.getTitle().equals(name)){
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
                        "(actor/production");
                switch (scanner.nextLine().trim()){
                    case "actor" -> {
                        System.out.println("Enter the name of the" +
                                " actor you want to delete from " +
                                "your" +
                                "favorites list: ");
                        String name = scanner.nextLine().trim();
                        for (Actor actor : actors){
                            if (actor.getName().equals(name)){
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

    public static void advancedOptions(int choice){
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
                    case 9 -> updateProductionInfo();
                    case 10 -> updateActorInfo();
                    case 11 -> {
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
                    case 8-> updateProductionInfo();
                    case 9 -> updateActorInfo();
                    case 10 -> manageUsers();
                    case 11 -> {
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
        System.out.println("TODO");
    }

    public static void solveRequests(){
        System.out.println("TODO");
    }

    public static void updateProductionInfo(){
        System.out.println("TODO");
    }

    public static void updateActorInfo(){
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
            appMode = scanner.nextLine().trim();

            if (appMode.equals("terminal")){
                activeUser = login();
                showOptions();
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
