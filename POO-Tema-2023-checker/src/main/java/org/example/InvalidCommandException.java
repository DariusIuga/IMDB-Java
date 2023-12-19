package org.example;

public class InvalidCommandException extends Exception{
    public InvalidCommandException(String exceptionMessage){
        super(exceptionMessage);
    }

    public InvalidCommandException(){
        super("Invalid command specified!");
    }
}
