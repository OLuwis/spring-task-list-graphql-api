package com.luwis.application.user.exceptions;

import org.springframework.graphql.execution.ErrorType;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;

public class UserRegisteredException extends RuntimeException {
    private String message = "Invalid Email: Email Is Already In Use";

    private String type = ErrorType.BAD_REQUEST.toString();

    public GraphQLError error() {
        return GraphqlErrorBuilder
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
