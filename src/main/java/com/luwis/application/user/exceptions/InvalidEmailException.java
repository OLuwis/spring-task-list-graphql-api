package com.luwis.application.user.exceptions;

import org.springframework.graphql.execution.ErrorType;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;

public class InvalidEmailException extends RuntimeException {
    public String message = "Invalid Email: Please Put A Valid Email";

    public String type = ErrorType.BAD_REQUEST.toString();
    
    public GraphQLError error() {
        return GraphqlErrorBuilder
            .newError()
            .errorType(ErrorType.BAD_REQUEST)
            .message(message)
            .build();
    }
}
