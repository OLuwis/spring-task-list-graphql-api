package com.luwis.application.todo.exceptions;

import org.springframework.graphql.execution.ErrorType;

import graphql.GraphQLError;

public class InvalidDescriptionException extends RuntimeException {
    public String message = "Invalid Description: Please Insert A Description";

    public String type = ErrorType.BAD_REQUEST.toString();

    public GraphQLError error() {
        return GraphQLError
            .newError()
            .errorType(ErrorType.BAD_REQUEST)
            .message(message)
            .build();
    }
}
