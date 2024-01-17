package org.example;

import java.util.*;

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
            user.getInformation().stringToLDT();
        }


        // Parse actors
        actors = deserializer.deserializeActors();

        // Parse productions
        productions = deserializer.deserializeProductions();

        // Parse requests
        requests = deserializer.deserializeRequests();
        for (Request request : requests){
            request.stringToLDT();
            if (request.getTo().equals("ADMIM")){
                Admin.RequestsHolder.addRequest(request);
            }
        }

        // Create the mixed collections for favorites and contributions (in
        // the case of Staff users)
        for (User<?> user : accounts){
            if (user.favoriteProductions != null){
                for (String productionString : user.favoriteProductions){
                    for (Production production : productions){
                        if (production.getTitle().equals(productionString)){
                            user.addToFavorites(production);
                        }
                    }
                }
            }
            if (user.favoriteActors != null){
                for (String actorString : user.favoriteActors){
                    for (Actor actor : actors){
                        if (actor.getName().equals(actorString)){
                            user.addToFavorites(actor);
                        }
                    }
                }
            }
            if (user instanceof Staff<?> staff){
                for (String productionsContribution :
                        staff.getProductionsContribution()){
                    for (Production production : productions){
                        if (production.getTitle().equals(productionsContribution)){
                            staff.addToContribution(production);
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
            System.out.println("\n\nChoose action:");
            System.out.println("\t1) View productions details");
            System.out.println("\t2) View actors details");
            System.out.println("\t3) View notifications");
            System.out.println("\t4) Search for actor/movie/series");
            System.out.println("\t5) Add/Delete actor/movie/series to/from " +
                    "favorites");

            switch (type){
                case REGULAR -> showRegularOptions((Regular<?>) activeUser);
                case CONTRIBUTOR ->
                        showContributorOptions((Contributor<?>) activeUser);
                case ADMIN -> showAdminOptions((Admin<?>) activeUser);
                default ->
                        System.err.println("Invalid user type when showing " +
                                "user options!");
            }

            // The user enters a number corresponding to his/her choice
            System.out.println("\nEnter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            handleOptionChoice(choice, activeUser);
        }
    }

    private static void showRegularOptions(Regular<?> activeUser){
        System.out.println("\t6) Add/Delete request");
        System.out.println("\t7) Add/Delete review for movies/series");
        System.out.println("\t8) Log Out");
    }

    private static void showContributorOptions(Contributor<?> activeUser){
        System.out.println("\t6) Add/Delete request");
        System.out.println("\t7) Add/Delete actor/movie/series to/from system");
        System.out.println("\t8) View and solve requests");
        System.out.println("\t9) Update movie/actor details");
        System.out.println("\t10) Log Out");
    }

    private static void showAdminOptions(Admin<?> activeUser){
        System.out.println("\t6) Add/Delete actor/movie/series to/from system");
        System.out.println("\t7) View and solve requests");
        System.out.println("\t8) Update movie/actor details");
        System.out.println("\t9) Add/Delete user");
        System.out.println("\t10) Log Out");
    }

    private static void handleOptionChoice(int choice, User<?> activeUser){
        switch (choice){
            case 1 -> System.out.println(productions);
            case 2 -> System.out.println(actors);
            case 3 -> System.out.println(activeUser.getNotifications());
            case 4 -> handleSearchOption();
            case 5 -> manageFavorites(activeUser);
            default -> advancedOptions(choice, activeUser);
        }
    }

    private static void handleSearchOption(){
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
            case "add" -> handleAddFavorite(activeUser);
            case "delete" -> handleDeleteFavorite(activeUser);
            default -> System.out.println("Enter 'add' or 'delete' please");
        }
    }

    private static void handleAddFavorite(User<?> activeUser){
        System.out.println("What do you want to add? (actor/production)");

        switch (scanner.nextLine().trim()){
            case "actor" -> addActorToFavorite(activeUser);
            case "production" -> addProductionToFavorite(activeUser);
            default ->
                    System.out.println("Enter 'actor' or 'production' please");
        }
    }

    private static void addActorToFavorite(User<?> activeUser){
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

    private static void addProductionToFavorite(User<?> activeUser){
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

    private static void handleDeleteFavorite(User<?> activeUser){
        System.out.println("What do you want to delete? (actor/production)");

        switch (scanner.nextLine().trim()){
            case "actor" -> deleteActorFromFavorite(activeUser);
            case "production" -> deleteProductionFromFavorite(activeUser);
            default ->
                    System.out.println("Enter 'actor' or 'production' please");
        }
    }

    private static void deleteActorFromFavorite(User<?> activeUser){
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

    private static void deleteProductionFromFavorite(User<?> activeUser){
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

    public static void advancedOptions(int choice, User<?> activeUser){
        AccountType type = activeUser.getUserType();

        switch (type){
            case REGULAR ->
                    handleRegularOptions(choice, (Regular<?>) activeUser);
            case CONTRIBUTOR -> handleContributorOptions(choice,
                    (Contributor<?>) activeUser);
            case ADMIN -> handleAdminOptions(choice, (Admin<?>) activeUser);
            default -> System.err.println("Error: Invalid user type");
        }
    }

    private static void handleRegularOptions(int choice, Regular<?> activeUser){
        switch (choice){
            case 6 -> manageRequests(activeUser);
            case 7 -> manageReviews();
            case 8 -> logout();
            default -> System.err.println("Invalid input when selecting " +
                    "options:" +
                    " please enter an integer between 1 and 8!");
        }
    }

    private static void handleContributorOptions(int choice,
                                                 Contributor<?> activeUser){
        switch (choice){
            case 6 -> manageRequests(activeUser);
            case 7 -> manageProductionsAndActors(activeUser);
            case 8 -> solveRequests(activeUser);
            case 9 -> updateInfo();
            case 10 -> logout();
            default ->
                    System.err.println("Invalid input when selecting options:" +
                            " please enter an integer between 1 and 11!");
        }
    }

    private static void handleAdminOptions(int choice, Admin<?> activeUser){
        switch (choice){
            case 6 -> manageProductionsAndActors(activeUser);
            case 7 -> solveRequests((activeUser));
            case 8 -> updateInfo();
            case 9 -> manageUsers(activeUser);
            case 10 -> logout();
            default ->
                    System.err.println("Invalid input when selecting options:" +
                            " please enter an integer between 1 and 11!");
        }
    }

    public static void manageRequests(User<?> activeUser){
        ArrayList<Request> myRequests = new ArrayList<>();
        for (Request request : requests){
            if (request.getUsername().equals(activeUser.getUsername())){
                myRequests.add(request);
            }
        }
        boolean isValid = true;

        System.out.println("These are your current submitted requests: " + myRequests);
        System.out.println("Do you want to create or delete a request " +
                "(add/delete)");
        String choiceString = scanner.nextLine().trim();
        switch (choiceString){
            case "add" -> {
                Request request;
                System.out.println("What type of request do you want to " +
                        "submit?");
                System.out.println("1) DELETE_ACCOUNT");
                System.out.println("2) ACTOR_ISSUE");
                System.out.println("3) MOVIE_ISSUE");
                System.out.println("4) OTHERS");
                RequestType type = RequestType.OTHERS;
                String name = null;

                int choice = 5;
                try{
                    choice = scanner.nextInt();
                    scanner.nextLine();
                } catch (Exception e){
                    System.err.println("Please enter a valid number!");
                }
                switch (choice){
                    case 1 -> type = RequestType.DELETE_ACCOUNT;
                    case 2 -> {
                        type = RequestType.ACTOR_ISSUE;
                        System.out.println("Please add the name of the actor " +
                                "too");
                        name = scanner.nextLine().trim();
                    }
                    case 3 -> {
                        type = RequestType.MOVIE_ISSUE;
                        System.out.println("Please add the name of the movie " +
                                "too");
                        name = scanner.nextLine().trim();
                    }
                    case 4 -> type = RequestType.OTHERS;
                    default -> {
                        try{
                            throw new InvalidCommandException("Please " +
                                    "enter a valid choice");
                        } catch (InvalidCommandException e){
                            e.printStackTrace();
                        }
                    }
                }

                System.out.println("Add a description: ");
                String description = scanner.nextLine().trim();
                if (name == null){
                    // The Request is of DELETE_ACCOUNT or OTHERS type
                    request = new Request(type, description);
                } else{
                    request = new Request(type, description, name);
                }
                request.setUsername(activeUser.getUsername());
                if (request.getTo().equals("ADMIN")){
                    Admin.RequestsHolder.addRequest(request);
                } else{
                    if (request.getTo().equals(request.getUsername())){
                        System.out.println("You can't write a request " +
                                "regarding an actor or movie that you added!");
                        isValid = false;
                    } else{
                        for (User<?> user : accounts){
                            if (user.getUsername().equals(request.getTo())){
                                Staff<?> staff = (Staff<?>) user;
                                staff.requestsToSolve.add(request);
                            }
                        }
                    }
                }

                if (isValid){
                    activeUser.subscribeToSubject(request);
                    request.subscribe(activeUser);
                    requests.add(request);
                }
            }
            case "delete" -> {
                if (myRequests.isEmpty()){
                    System.out.println("You have no current requests!");
                    break;
                }
                System.out.println("Which request do you want do delete?");
                for (int i = 1; i <= myRequests.size(); i++){
                    System.out.println(i + ") " + myRequests.get(i - 1));
                }
                int choice;
                do{
                    choice = scanner.nextInt();
                    scanner.nextLine();
                    if (choice <= 0 || choice > myRequests.size()){
                        System.err.println("Enter a valid request index!");
                    }
                    break;
                } while (true);
                choice--;

                Request request = myRequests.get(choice);
                activeUser.unsubscribeFromSubject(myRequests.get(choice));
                request.unsubscribe(activeUser);
                requests.remove(request);

                if (request.getTo().equals("ADMIN")){
                    Admin.RequestsHolder.deleteRequest(request);
                } else{
                    for (User<?> user : accounts){
                        if (user.getUsername().equals(request.getUsername())){
                            Staff<?> staff = (Staff<?>) user;
                            staff.requestsToSolve.remove(request);
                        }
                    }
                }
            }
            default -> {
                System.err.println("Invalid command");
            }
        }
    }

    public static void manageProductionsAndActors(Staff<?> activeUser){
        // TODO: Combine productions and actors

        System.out.println("What do you want to change (productions/actors)");
        switch (scanner.nextLine().trim()){
            case "productions" -> {
                System.out.println("These are all the current registered " +
                        "productions:\n" + productions);
                System.out.println("These are all the productions added by " +
                        "you " + activeUser.getProductionsContribution());
                System.out.println("Do you want to add or delete a " +
                        "production? (add/delete)");

                switch (scanner.nextLine().trim()){
                    case "add" -> {
                        addProduction(activeUser);
                    }
                    case "delete" -> {
                        System.out.println("Enter the name of the " +
                                "movie/series that you want to delete: ");
                        String name = scanner.nextLine().trim();
                        activeUser.removeProductionSystem(name);
                    }

                }
            }
            case "actors" -> {
                System.out.println("These are all the current registered " +
                        "actors:\n" + actors);
                System.out.println("These are all the actors added by " +
                        "you " + activeUser.getActorsContribution());
                System.out.println("Do you want to add or delete am " +
                        "actor? (add/delete)");

                switch (scanner.nextLine().trim()){
                    case "add" -> {
                        addActor(activeUser);
                    }
                    case "delete" -> {
                        System.out.println("Enter the name of the " +
                                "actor that you want to delete: ");
                        String name = scanner.nextLine().trim();
                        activeUser.removeActorSystem(name);
                    }

                }
            }
            default -> System.err.println("Please enter a valid option! " +
                    "(productions/actors)");
        }
    }

    public static void addProduction(Staff<?> activeUser){
        System.out.println("Enter title: ");
        String title = scanner.nextLine().trim();
        System.out.println("Enter list of directors. Write " +
                "'done' to stop. ");
        int nrDirectors = 1;
        ArrayList<String> directors = new ArrayList<>();
        while (true){
            System.out.println(nrDirectors);
            String director = scanner.nextLine().trim();
            if (director.equals("done")){
                break;
            }
            directors.add(director);
            nrDirectors++;
        }

        System.out.println("Enter list of actors. Write " +
                "'done' to stop. ");
        int nrActors = 1;
        ArrayList<String> actors = new ArrayList<>();
        while (true){
            System.out.println(nrActors);
            String actor = scanner.nextLine().trim();
            if (actor.equals("done")){
                break;
            }
            actors.add(actor);
            nrActors++;
        }

        System.out.println("Enter list of genres. " +
                "Write 'done' to stop.");
        int nrGenres = 1;
        ArrayList<Genre> genres = new ArrayList<>();
        while (true){
            try{
                System.out.println(nrGenres);
                String genre = scanner.nextLine().trim();
                if (genre.equals("done")){
                    break;
                }
                genres.add(Genre.valueOf(genre.toUpperCase()));
                nrGenres++;
            } catch (Exception e){
                System.out.println("Enter a valid genre name," +
                        " or 'other'!");
            }
        }

        System.out.println("The list of ratings is initially " +
                "empty.");
        ArrayList<Rating> ratings = new ArrayList<>();
        System.out.println("Add a description of the plot: ");
        String plot = scanner.nextLine().trim();

        while (true){
            System.out.println("What type of production do " +
                    "you " +
                    "want to submit (movie/series)");

            String type = scanner.nextLine().trim();
            System.out.println("Enter the movie's release" +
                    " year: ");
            short releaseYear = scanner.nextShort();
            scanner.nextLine();

            if (type.equals("movie")){
                System.out.println("Enter the movie's " +
                        "duration in this format: <x " +
                        "mimnutes>");
                String duration = scanner.nextLine().trim();

                Movie movie = new Movie(title, directors,
                        actors, genres, ratings, plot, 0,
                        duration
                        , releaseYear);
                activeUser.addProductionSystem(movie);
                System.out.println("\nYou submitted this " +
                        "movie:" +
                        " " + movie);

                break;
            } else if (type.equals("series")){
                System.out.println("Enter the number of " +
                        "seasons: ");
                int numSeasons = scanner.nextInt();
                scanner.nextLine();


                Map<String, List<Episode>> seasons =
                        new HashMap<>();
                for (int i = 1; i <= numSeasons; i++){
                    System.out.println("Entering details for " +
                            "the episodes in the current " +
                            "season. Write 'done' to finish.");
                    List<Episode> episodesInSeason =
                            new ArrayList<>();

                    int j = 1;
                    while (true){
                        System.out.println("Episode number " + j + " of this " +
                                "season");
                        System.out.println("Episode name: ");
                        String episodeName =
                                scanner.nextLine().trim();
                        if (episodeName.equals("done")){
                            break;
                        }

                        System.out.println("Duration: ");
                        String duration =
                                scanner.nextLine().trim();
                        if (duration.equals("done")){
                            break;
                        }
                        j++;
                        episodesInSeason.add(new Episode(episodeName,
                                duration));
                        seasons.put("Season " + i,
                                episodesInSeason);
                    }
                }

                Series series = new Series(title, directors,
                        actors, genres, ratings, plot, 0,
                        releaseYear, numSeasons, seasons);
                activeUser.addProductionSystem(series);
                System.out.println("\nYou submitted this " +
                        "series: " + series);

                break;
            } else{
                System.err.println("Choose from movie or " +
                        "series!");
            }
        }
    }

    public static void addActor(Staff<?> activeUser){
        System.out.println("Enter the actor/actress's name: ");
        String name = scanner.nextLine().trim();
        System.out.println("Enter the roles that " +
                "he/she played in. Type 'done' when you are " +
                "satisfied.");
        ArrayList<Performance> performances = new ArrayList<>();
        int nr = 1;
        while (true){
            System.out.println("Performance nr " + nr);
            System.out.println("Title: ");
            String title = scanner.nextLine().trim();
            if (title.equals("done")){
                break;
            }
            System.out.println("Genre: ");
            String genre = scanner.nextLine().trim();
            if (genre.equals("done")){
                break;
            }
            performances.add(new Performance(title, genre));
            nr++;
        }
        System.out.println("Finally, add a biography");
        String biography = scanner.nextLine().trim();

        Actor actor = new Actor(name, performances, biography);
        activeUser.addActorSystem(actor);
        System.out.println("\nYou submitted this " +
                "actor/actress: " + actor);
    }

    public static void solveRequests(Staff<?> activeUser){
        if(activeUser.requestsToSolve.isEmpty()){
            System.out.println("You have no requests to view!");
            return;
        }
        System.out.println("Current requests for this user: ");
        int i = 1;
        for (Request request : activeUser.requestsToSolve){
            System.out.println(i + ") " + request);
            i++;
        }

        int choice = 0;
        do{
            System.out.println("\nWhich request do you want to inspect?");
            choice = scanner.nextInt();
            scanner.nextLine();
            if (choice < 1 || choice > i){
                System.err.println("Enter a valid request number please!");
            } else{
                break;
            }
        }
        while (true);
        choice--;

        Request request = activeUser.requestsToSolve.get(choice);
        System.out.println("Do you want to solve this request? (yes/no)");
        switch (scanner.nextLine().trim()){
            case "yes" -> {
                request.notifyObservers("Your previous request: " + request +
                        " was solved by staff member " + activeUser.getUsername());
                if (request.getType().equals(RequestType.DELETE_ACCOUNT)){
                    System.out.println("Todo: Delete account");
                } else{
                    System.out.println("\nHere is the request you selected: " + request);
                    // TODO: Update info
                }

                //TODO: add experience
            }
            case "no" -> {
                request.notifyObservers("Your previous request: " + request +
                        " was refused by staff member " + activeUser.getUsername());
            }
            default -> System.out.println("Enter a valid option please!");
        }
        if (choice == 0){
            System.out.println("Todo: Delete account");
        } else{
            System.out.println("\nHere is the request you selected: " + request);
        }

        for (User<?> user : accounts){
            if (user.getUsername().equals(request.getUsername())){
                request.unsubscribe(user);
                user.unsubscribeFromSubject(request);
            }
        }
        activeUser.requestsToSolve.remove(request);
        requests.remove(request);
        Admin.RequestsHolder.deleteRequest(request);

    }

    public static void updateInfo(){
        System.out.println("TODO");
    }

    public static void manageReviews(){
        System.out.println("TODO");
    }

    public static void manageUsers(Admin<?> currentUser){
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
                showOptions(activeUser);
                break;
            } else if (appMode.equals("gui")){
                System.out.println("Sorry, I haven't implemented a user " +
                        "interface" +
                        " " +
                        "yet...");
                break;
            }
        }

        System.out.println("ACCOUNTS:\n\n" + accounts);
        System.out.println("ACTORS:\n\n" + actors);
        System.out.println("PRODUCTIONS:\n\n" + productions);
        System.out.println("REQUESTS:\n\n" + requests);
    }
}
