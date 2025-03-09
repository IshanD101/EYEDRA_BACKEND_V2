package com.eyedra.reaction_service_api.exception;

public class ReactionNotFoundException extends RuntimeException {
    public ReactionNotFoundException(String message) {
        super(message);
    }
}
