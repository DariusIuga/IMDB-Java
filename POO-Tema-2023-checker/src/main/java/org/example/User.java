package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "userType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Regular.class, name = "Regular"),
        @JsonSubTypes.Type(value = Contributor.class, name = "Contributor"),
        @JsonSubTypes.Type(value = Admin.class, name = "Admin")}
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class User<T extends Comparable<T>> implements Observer{

    private Information information;
    private AccountType userType;
    private String username;
    private int experience;
    private List<String> notifications;
    private SortedSet<CommonInterface> favorites = new TreeSet<>();
    ArrayList<String> favoriteProductions;
    ArrayList<String> favoriteActors;

    private ExperienceStrategy strategy;


    public User(){

    }

    public User(Information information, AccountType userType,
                String username, int experience, List<String> notifications,
                SortedSet<CommonInterface> favorites){
        this.information = information;
        this.userType = userType;
        this.username = username;
        this.experience = experience;
        this.notifications = notifications;
        this.favorites = favorites;
    }

    public ExperienceStrategy getStrategy(){
        return strategy;
    }

    public void setStrategy(ExperienceStrategy strategy){
        this.strategy = strategy;
        this.experience += strategy.calculateExperience();
    }

    public Information getInformation(){
        return information;
    }

    public void setInformation(Information information){
        this.information = information;
    }

    public AccountType getUserType(){
        return userType;
    }

    public void setUserType(AccountType userType){
        this.userType = userType;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public int getExperience(){
        return experience;
    }

    public void setExperience(int experience){
        this.experience = experience;
    }

    public List<String> getNotifications(){
        return notifications;
    }

    public void setNotifications(List<String> notifications){
        this.notifications = notifications;
    }

    public SortedSet<CommonInterface> getFavorites(){
        return favorites;
    }

    public void setFavorites(SortedSet<CommonInterface> commonInterfaces){
        this.favorites = commonInterfaces;
    }

    public void addToFavorites(CommonInterface obj){
        favorites.add(obj);
    }

    public void deleteFromFavorites(CommonInterface obj){
        favorites.remove(obj);
    }

    public void logOut(){
        User<?> activeUser = IMDB.login();
        IMDB.showOptions(activeUser);
    }

    public void subscribeToSubject(Subject subject) {
        subject.subscribe(this);
    }

    public void unsubscribeFromSubject(Subject subject) {
        subject.unsubscribe(this);
    }

    // Called by subjects (Rating and Request objects) when some events happen
    public void update(String message){
        this.notifications.add(message);
    }

    public ArrayList<String> getFavoriteProductions(){
        return favoriteProductions;
    }

    public void setFavoriteProductions(ArrayList<String> favoriteProductions){
        this.favoriteProductions = favoriteProductions;
    }

    public ArrayList<String> getFavoriteActors(){
        return favoriteActors;
    }

    public void setFavoriteActors(ArrayList<String> favoriteActors){
        this.favoriteActors = favoriteActors;
    }

    @Override
    public String toString(){
        return "User{" +
                "information=" + information +
                ", userType=" + userType +
                ", username='" + username + '\'' +
                ", experience=" + experience +
                ", notifications=" + notifications +
                ", favorites=" + favorites +
                ", favoriteProductions=" + favoriteProductions +
                ", favoriteActors=" + favoriteActors +
                "}\n";
    }


    @JsonDeserialize(using = InformationDeserializer.class)
    static class Information{
        private static final DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd");
        private final Credentials credentials;
        private final String name;
        private final String country;
        private final short age;
        private final char gender;
        private final String birthDate;
        private LocalDateTime date;

        public void stringToLDT(){
            if (birthDate != null){
                this.date = LocalDate.parse(birthDate, formatter).atStartOfDay();
            }
        }

        public LocalDateTime getDate(){
            return date;
        }

        public void setDate(LocalDateTime date){
            this.date = date;
        }

        private Information(InformationBuilder builder){
            this.credentials = builder.credentials;
            this.name = builder.name;
            this.country = builder.country;
            this.age = builder.age;
            this.gender = builder.gender;
            this.birthDate = builder.birthDate;
        }

        public Credentials getCredentials(){
            return credentials;
        }

        public String getName(){
            return name;
        }

        public String getCountry(){
            return country;
        }

        public short getAge(){
            return age;
        }

        public char getGender(){
            return gender;
        }

        public String getBirthDate(){
            return birthDate;
        }

        @Override
        public String toString(){
            return "Information{" +
                    "credentials=" + credentials +
                    ", name='" + name + '\'' +
                    ", country='" + country + '\'' +
                    ", age=" + age +
                    ", gender=" + gender +
                    ", birthDate=" + birthDate +
                    ", date=" + date +
                    "}\n";
        }

        static class InformationBuilder{
            private final Credentials credentials;
            private final String name;
            private String country;
            private short age;
            private char gender;
            private LocalDateTime date;

            // JSON wrapper
            private String birthDate;

            public InformationBuilder(Credentials credentials, String name){
                this.credentials = credentials;
                this.name = name;
            }

            public InformationBuilder country(String country){
                this.country = country;
                return this;
            }

            public InformationBuilder age(short age){
                this.age = age;
                return this;
            }

            public InformationBuilder gender(char gender){
                this.gender = gender;
                return this;
            }

            public InformationBuilder birthDate(String birthDate){
                this.birthDate = birthDate;
                return this;
            }

            Information build(){
                return new Information(this);
            }
        }
    }
}
