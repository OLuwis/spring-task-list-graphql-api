package com.luwis.application.user.exceptions;

import org.springframework.graphql.execution.ErrorType;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;

public class InvalidUsernameException extends RuntimeException {
    public String message = "Invalid Username: Usernames Must Be Between 3-20 Characters Long";

    public String type = ErrorType.BAD_REQUEST.toString();
    
    public GraphQLError error() {
        return GraphqlErrorBuilder
        .newError()
        .errorType(ErrorType.BAD_REQUEST)
        .message(message)
        .build();
    }
}
