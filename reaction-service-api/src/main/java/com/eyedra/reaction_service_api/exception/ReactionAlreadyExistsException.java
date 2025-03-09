package com.eyedra.reaction_service_api.exception;

public class ReactionAlreadyExistsException extends RuntimeException {
    public ReactionAlreadyExistsException(String message) {
        super(message);
    }
}
