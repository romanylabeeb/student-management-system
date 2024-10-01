package com.boubyan.api.exception;
// Exception forDuplicate User

public class DuplicateUserException extends CustomException {
    public DuplicateUserException(String username) {
        super("User with username '" + username + "' already exists.");
    }
}
