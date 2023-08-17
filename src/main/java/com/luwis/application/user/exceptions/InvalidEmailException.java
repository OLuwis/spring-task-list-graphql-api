package com.luwis.application.user.exceptions;

import org.springframework.graphql.execution.ErrorType;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;

public class InvalidEmailException extends RuntimeException {
    public GraphQLError error() {
        return GraphqlErrorBuilder
            .newError()
            .errorType(ErrorType.BAD_REQUEST)
            .message("Invalid Email: Please Put A Valid Email")
            .build();
    }
}
