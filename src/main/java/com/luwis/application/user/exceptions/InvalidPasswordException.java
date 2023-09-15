package com.luwis.application.user.exceptions;

import org.springframework.graphql.execution.ErrorType;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;

public class InvalidPasswordException extends RuntimeException {
    public String message = "Invalid Password: Passwords Must Be Between 8-20 Characters Long And Contain A Number, An Upper And Lowercase Letter, And A Special Symbol";

    public String type = ErrorType.BAD_REQUEST.toString();

    public GraphQLError error() {
        return GraphqlErrorBuilder
            .newError()
            .errorType(ErrorType.BAD_REQUEST)
            .message(message)
            .build();
    }
}
