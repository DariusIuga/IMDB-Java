package org.example;

public class InformationIncompleteException extends Exception{

    public InformationIncompleteException(String errorMessage){
        super(errorMessage);
    }

    public InformationIncompleteException(){
        super("You must enter a name and credentials when instantiating an " +
                "Information Object!");
    }
}
