package org.example;

import java.util.*;
import java.util.stream.Collectors;

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
            case 1 -> {
                System.out.println("Do you want to filter productions based " +
                        "on type? (y/n)");
                if (scanner.nextLine().trim().equals("y")){
                    System.out.println("What do you want to see? " +
                            "(movies/series)");
                    switch (scanner.nextLine().trim()){
                        case "movies" -> {
                            System.out.println(productions.stream().filter(production -> production instanceof Movie).collect(Collectors.toList()));
                        }
                        case "series" -> {
                            System.out.println(productions.stream().filter(production -> production instanceof Series).collect(Collectors.toList()));
                        }
                        default -> System.err.println("Enter a valid option " +
                                "please!");
                    }
                } else{
                    System.out.println("Do you want to filter productions " +
                            "based " +
                            "on the number of reviews? (y/n)");
                    if (scanner.nextLine().trim().equals("y")){
                        System.out.println("What should be the minimum number" +
                                " of " +
                                "reviews for a production in order to show " +
                                "it?");
                        int n = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println(productions.stream().filter(production -> production.getRatings().size() >= n).collect(Collectors.toList()));
                    } else{
                        System.out.println(productions);
                    }
                }

            }
            case 2 -> {
                System.out.println("Do you want to sort the actors " +
                        "alphabetically? (y/n)");
                if (scanner.nextLine().trim().equals("y")){
                    System.out.println(actors.stream()
                            .sorted(Comparator.comparing(Actor::getName))
                            .toList());
                } else{
                    System.out.println(actors);
                }

            }
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
            case 7 -> manageReviews(activeUser);
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
                        activeUser.setStrategy(new ContributionGain());
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
                        activeUser.setStrategy(new ContributionGain());
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
        if (activeUser.requestsToSolve.isEmpty()){
            System.out.println("You have no requests to view!");
            return;
        }
        System.out.println("Current requests for this user: ");
        int i = 1;
        for (Request request : activeUser.requestsToSolve){
            System.out.println(i + ") " + request);
            i++;
        }
        i -= 2;

        int choice = 0;
        do{
            System.out.println("\nWhich request do you want to inspect?");
            choice = scanner.nextInt();
            scanner.nextLine();
            if (choice < 1 || choice > i + 1){
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
                switch (request.getType()){
                    case DELETE_ACCOUNT -> {
                        for (User<?> user : accounts){
                            if (user.getUsername().equals(request.getUsername())){
                                deleteAccount(user, (Admin<?>) activeUser);
                            }
                        }
                    }
                    case ACTOR_ISSUE,MOVIE_ISSUE -> {
                        for (User<?> user : accounts){
                            if (user.getUsername().equals(request.getUsername())){
                                user.setStrategy(new AcceptedRequestGain());
                            }
                        }
                        updateInfo();
                    }
                    case OTHERS -> {
                        updateInfo();
                    }
                    default -> {
                        System.err.println("Invalid request type!");
                    }
                }
                if (request.getType().equals(RequestType.DELETE_ACCOUNT)){
                    for (User<?> user : accounts){
                        if (user.getUsername().equals(request.getUsername())){
                            deleteAccount(user, (Admin<?>) activeUser);
                        }
                    }
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
        do{
            System.out.println("What do you want to update? " +
                    "(production/actor)\n " +
                    "Type 'done' to exit.");
            switch (scanner.nextLine().trim()){
                case "production" -> {
                    System.out.println("These are all the current " +
                            "added productions: ");
                    int i = 1;
                    for (Production production : productions){
                        System.out.println(i + ") " + production);
                        i++;
                    }
                    i -= 2;

                    int choice = 0;
                    do{
                        System.out.println("\nWhat production do you want to " +
                                "change?");
                        choice = scanner.nextInt();
                        scanner.nextLine();
                        if (choice < 1 || choice > i + 1){
                            System.err.println("Enter a valid production " +
                                    "number please!");
                        } else{
                            break;
                        }
                    }
                    while (true);
                    choice--;

                    updateProduction(productions.get(i));
                }
                case "actor" -> {
                    System.out.println("These are all the current " +
                            "added actors: ");
                    int i = 1;
                    for (Actor actor : actors){
                        System.out.println(i + ") " + actor);
                        i++;
                    }
                    i -= 2;

                    int choice = 0;
                    do{
                        System.out.println("\nWhat actor do you want to " +
                                "change?");
                        choice = scanner.nextInt();
                        scanner.nextLine();
                        if (choice < 1 || choice > i + 1){
                            System.err.println("Enter a valid actor " +
                                    "number please!");
                        } else{
                            break;
                        }
                    }
                    while (true);
                    choice--;

                    updateActor(actors.get(i));
                }
                case "done" -> {
                    return;
                }
                default -> System.err.println("Enter a valid choice please!");
            }
        } while (true);
    }

    public static void updateProduction(Production production){
        do{
            System.out.println("What to change?");
            System.out.println("1) Title");
            System.out.println("2) Directors");
            System.out.println("3) Actors");
            System.out.println("4) Genres");
            System.out.println("5) Plot");
            System.out.println("6) Release Year");
            System.out.println("7) Done");


            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice){
                case 1 -> {
                    System.out.println("Title: ");
                    String title = scanner.nextLine().trim();
                    production.setTitle(title);
                }
                case 2 -> {
                    System.out.println("These are the directors for this " +
                            "production: ");
                    int i = 0;
                    for (String director : production.getDirectors()){
                        i++;
                        System.out.println(i + ") " + director);
                    }


                    System.out.println("Do you want to add,change or delete a" +
                            " performance? (add/change/delete)");
                    switch (scanner.nextLine().trim()){
                        case "add" -> {
                            System.out.println("New director name: ");
                            String name = scanner.nextLine().trim();
                            production.getDirectors().add(name);
                        }
                        case "change" -> {
                            int option = 0;
                            do{
                                System.out.println("\nWhat director do you" +
                                        " want to " +
                                        "change?");
                                option = scanner.nextInt();
                                scanner.nextLine();
                                if (option < 1 || option > i){
                                    System.err.println("Enter a valid " +
                                            "director" +
                                            " number please!");
                                } else{
                                    break;
                                }
                            }
                            while (true);
                            option--;

                            System.out.println("New director name: ");
                            String name = scanner.nextLine().trim();
                            production.getDirectors().set(i - 1, name);
                        }
                        case "delete" -> {
                            int option = 0;
                            do{
                                System.out.println("\nWhat director do you" +
                                        " want to " +
                                        "delete?");
                                option = scanner.nextInt();
                                scanner.nextLine();
                                if (option < 1 || option > i){
                                    System.err.println("Enter a valid " +
                                            "director" +
                                            " " +
                                            "number please!");
                                } else{
                                    break;
                                }
                            }
                            while (true);
                            option--;

                            production.getDirectors().remove(i - 1);
                        }
                        default -> System.err.println("Please enter a valid " +
                                "option!");
                    }
                }
                case 3 -> {
                    System.out.println("These are the actors for this " +
                            "production: ");
                    int i = 0;
                    for (String actor : production.getActors()){
                        i++;
                        System.out.println(i + ") " + actor);
                    }


                    System.out.println("Do you want to add,change or delete " +
                            "an" +
                            " actor? (add/change/delete)");
                    switch (scanner.nextLine().trim()){
                        case "add" -> {
                            System.out.println("New actor name: ");
                            String name = scanner.nextLine().trim();
                            production.getActors().add(name);
                        }
                        case "change" -> {
                            int option = 0;
                            do{
                                System.out.println("\nWhat actor do you" +
                                        " want to " +
                                        "change?");
                                option = scanner.nextInt();
                                scanner.nextLine();
                                if (option < 1 || option > i){
                                    System.err.println("Enter a valid " +
                                            "actor" +
                                            " number please!");
                                } else{
                                    break;
                                }
                            }
                            while (true);
                            option--;

                            System.out.println("New actor name: ");
                            String name = scanner.nextLine().trim();
                            production.getActors().set(i - 1, name);
                        }
                        case "delete" -> {
                            int option = 0;
                            do{
                                System.out.println("\nWhat actor do you" +
                                        " want to " +
                                        "delete?");
                                option = scanner.nextInt();
                                scanner.nextLine();
                                if (option < 1 || option > i){
                                    System.err.println("Enter a valid " +
                                            "actor" +
                                            " " +
                                            "number please!");
                                } else{
                                    break;
                                }
                            }
                            while (true);
                            option--;

                            production.getActors().remove(i - 1);
                        }
                        default -> System.err.println("Please enter a valid " +
                                "option!");
                    }
                }
                case 4 -> {
                    System.out.println("These are the genres for this " +
                            "production: ");
                    int i = 0;
                    for (Genre genre : production.getGenres()){
                        i++;
                        System.out.println(i + ") " + genre);
                    }

                    System.out.println("\nThese are all the registered genre " +
                            "names. Other is used for any other entered " +
                            "genre: ACTION,\n" +
                            "    ADVENTURE,\n" +
                            "    COMEDY,\n" +
                            "    DRAMA,\n" +
                            "    HORROR,\n" +
                            "    SF,\n" +
                            "    FANTASY,\n" +
                            "    ROMANCE,\n" +
                            "    MYSTERY,\n" +
                            "    THRILLER,\n" +
                            "    CRIME,\n" +
                            "    BIOGRAPHY,\n" +
                            "    WAR,\n" +
                            "    COOKING,\n" +
                            "    OTHER");
                    System.out.println("Do you want to add or delete " +
                            "a" +
                            " genre? (add/delete)");
                    switch (scanner.nextLine().trim()){
                        case "add" -> {
                            System.out.println("New genre name: ");
                            String name = scanner.nextLine().trim();
                            Genre genre;
                            try{
                                genre = Genre.valueOf(name.toUpperCase());
                            } catch (IllegalArgumentException e){
                                genre = Genre.OTHER;
                            }
                            production.addGenre(genre);
                        }
                        case "delete" -> {
                            System.out.println("\nWhat genre do you" +
                                    " want to " +
                                    "delete?");
                            String name = scanner.nextLine().trim();
                            Genre genre;
                            try{
                                genre = Genre.valueOf(name.toUpperCase());
                            } catch (IllegalArgumentException e){
                                genre = Genre.OTHER;
                            }
                            production.getGenres().remove(genre);
                        }
                        default -> System.err.println("Please enter a valid " +
                                "option!");
                    }
                }
                case 5 -> {
                    System.out.println("Plot: ");
                    String plot = scanner.nextLine().trim();
                    production.setPlot(plot);
                }
                case 6 -> {
                    System.out.println("Release Year: ");
                    short releaseYear = scanner.nextShort();
                    scanner.nextLine();
                    production.setReleaseYear(releaseYear);
                }
                case 7 -> {
                    return;
                }
                default -> {
                    System.err.println("Please enter a valid option");
                }
            }
        } while (true);
    }

    public static void updateActor(Actor actor){
        do{
            System.out.println("What to change?");
            System.out.println("1) Name");
            System.out.println("2) Performances");
            System.out.println("3) Biography");
            System.out.println("4) Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice){
                case 1 -> {
                    System.out.println("Name: ");
                    String name = scanner.nextLine().trim();
                    actor.setName(name);
                }
                case 2 -> {
                    System.out.println("These are all of the movies/series " +
                            "that this actor has ever starred in: ");
                    int i = 0;
                    for (Performance performance : actor.getPerformances()){
                        i++;
                        System.out.println(i + ") " + performance);
                    }
                    System.out.println("Do you want to add,change or delete a" +
                            " performance? (add/change/delete)");
                    switch (scanner.nextLine().trim()){
                        case "add" -> {
                            System.out.println("Title: ");
                            String title = scanner.nextLine().trim();
                            System.out.println("Type: ");
                            String type = scanner.nextLine().trim();
                            Performance performance = new Performance(title,
                                    type);
                            actor.addPerformance(performance);
                        }
                        case "change" -> {
                            int option = 0;
                            do{
                                System.out.println("\nWhat performance do you" +
                                        " want to " +
                                        "change?");
                                option = scanner.nextInt();
                                scanner.nextLine();
                                if (option < 1 || option > i){
                                    System.err.println("Enter a valid " +
                                            "performance " +
                                            "number please!");
                                } else{
                                    break;
                                }
                            }
                            while (true);
                            option--;

                            Performance performance =
                                    actor.getPerformance(option);
                            System.out.println("\nThis is the performance " +
                                    "that you selected: " + performance);
                            System.out.println("Title: ");
                            String title = scanner.nextLine().trim();
                            performance.setTitle(title);
                            System.out.println("Type: ");
                            String type = scanner.nextLine().trim();
                            performance.setType(type);
                        }
                        case "delete" -> {
                            int option = 0;
                            do{
                                System.out.println("\nWhat performance do you" +
                                        " want to " +
                                        "delete?");
                                option = scanner.nextInt();
                                scanner.nextLine();
                                if (option < 1 || option > i){
                                    System.err.println("Enter a valid " +
                                            "performance" +
                                            " " +
                                            "number please!");
                                } else{
                                    break;
                                }
                            }
                            while (true);
                            option--;

                            actor.getPerformances().remove(option);
                        }
                        default -> System.err.println("Please enter a valid " +
                                "option!");
                    }
                }
                case 3 -> {
                    System.out.println("Biography: ");
                    String biography = scanner.nextLine().trim();
                    actor.setBiography(biography);
                }
                case 4 -> {
                    return;
                }
                default -> {
                    System.err.println("Please enter a valid option");
                }
            }
        } while (true);
    }


    public static void manageReviews(Regular<?> activeUser){
        int option = 0;
        do{
            System.out.println("Which production do you want to leave a " +
                    "review " +
                    "for?\n");
            int i = 0;
            for (Production production : productions){
                i++;
                System.out.println(i + "( " + production);
            }
            option = scanner.nextInt();
            scanner.nextLine();
            if (option < 1 || option > i){
                System.err.println("Enter a valid " +
                        "production" +
                        " " +
                        "number please!");
            } else{
                break;
            }
        }
        while (true);
        option--;

        Production production = productions.get(option);

        System.out.println("Rating (must be between 1-10): ");
        byte rating = scanner.nextByte();
        scanner.nextLine();
        System.out.println("Leave a comment: ");
        String comment = scanner.nextLine().trim();
        Rating review = new Rating(activeUser.getUsername(), rating, comment);

        review.subscribe(activeUser);
        activeUser.subscribeToSubject(review);
        activeUser.addRating(production,review);
    }

    public static void manageUsers(Admin<?> activeUser){
        System.out.println("\nThese are all the users currently added to the " +
                "system:\n");
        int i = 0;
        for (User<?> user : accounts){
            i++;
            System.out.println(i + ") " + user);
        }
        System.out.println("\nDo you want to add a new user or to delete an " +
                "existing one? (add/delete)");
        switch (scanner.nextLine().trim()){
            case "add" -> {

            }
            case "delete" -> {
                int option = 0;
                do{
                    System.out.println("Which one do you want to delete?\n");
                    option = scanner.nextInt();
                    scanner.nextLine();
                    if (option < 1 || option > i){
                        System.err.println("Enter a valid " +
                                "user" +
                                " " +
                                "number please!");
                    } else{
                        break;
                    }
                }
                while (true);
                option--;

                User<?> user = accounts.get(option);
                deleteAccount(user, activeUser);
            }
            default -> {
                System.err.println("Please enter a valid option!");
            }
        }
    }

    public static void deleteAccount(User<?> user, Admin<?> admin){
        if (user.getUserType().equals(AccountType.CONTRIBUTOR)){
            Contributor<?> contributor = (Contributor<?>) user;

            for (String name :
                    contributor.getProductionsContribution()){
                contributor.removeProductionSystem(name);

                for (Production production : productions){
                    if (production.getName().equals(name)){
                        admin.addProductionSystem(production);
                    }
                }
            }
        }
        accounts.remove(user);
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
                System.out.println("Sorry, I haven't implemented a " +
                        "graphic user " +
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
