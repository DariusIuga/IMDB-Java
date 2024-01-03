package org.example;

public class UserFactory<T extends Comparable<T> >{
    public User<T> getUser(AccountType type){
        return switch (type){
            case REGULAR -> new Regular<>();
            case CONTRIBUTOR -> new Contributor<>();
            case ADMIN -> new Admin<>();
            default -> {
                try{
                    throw new Exception("Wrong user type provided!");
                } catch (Exception e){
                    throw new RuntimeException(e);
                }
            }
        };
    }
}
