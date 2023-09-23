package com.luwis.application.graphql.resolvers;

import org.springframework.graphql.execution.ErrorType;

import graphql.GraphQLError;
import graphql.execution.ResultPath;

public class BadCredentialsResolver extends RuntimeException {
    public GraphQLError resolve(String message, ResultPath path) {
        return GraphQLError.newError()
            .message(message)
            .errorType(ErrorType.BAD_REQUEST)
            .path(path)
            .build();
    }
}