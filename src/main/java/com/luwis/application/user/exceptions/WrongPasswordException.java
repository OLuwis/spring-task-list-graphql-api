package com.luwis.application.user.exceptions;

import org.springframework.graphql.execution.ErrorType;

import graphql.GraphQLError;

public class WrongPasswordException extends RuntimeException {
    private String message = "Wrong Password";

    private String type = ErrorType.BAD_REQUEST.toString();
    
    public GraphQLError error() {
        return GraphQLError
        .newError()
        .errorType(ErrorType.BAD_REQUEST)
        .message(message)
        .build();
    }

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }
}
