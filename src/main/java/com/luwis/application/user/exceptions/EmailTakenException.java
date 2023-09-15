package com.luwis.application.user.exceptions;

import org.springframework.graphql.execution.ErrorType;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;

public class EmailTakenException extends RuntimeException {
    public String message = "Invalid Email: Email Is Already In Use";

    public String type = ErrorType.BAD_REQUEST.toString();

    public GraphQLError error() {
        return GraphqlErrorBuilder
            .newError()
            .errorType(ErrorType.BAD_REQUEST)
            .message(message)
            .build();
    }
}
