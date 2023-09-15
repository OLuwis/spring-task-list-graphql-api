package com.luwis.application.user.exceptions;

import org.springframework.graphql.execution.ErrorType;

import graphql.GraphQLError;

public class WrongPasswordException extends RuntimeException {
    public String message = "Wrong Password";

    public String type = ErrorType.BAD_REQUEST.toString();
    
    public GraphQLError error() {
        return GraphQLError
            .newError()
            .errorType(ErrorType.BAD_REQUEST)
            .message(message)
            .build();
    }
}
