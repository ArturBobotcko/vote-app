package org.somecompany.exceptions;

public class UsernameTakenException extends Exception {
    public UsernameTakenException(String errorMessage) {
        super(errorMessage);
    }
}