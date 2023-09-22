package com.luwis.application.graphql.resolvers;

import org.springframework.graphql.execution.ErrorType;

import graphql.GraphQLError;
import graphql.execution.ResultPath;

public class BadCredentialsResolver extends RuntimeException {
    private final String message = " Error: This password is incorrect";

    public GraphQLError resolve(ResultPath path) {
        String prefix = path.toString().substring(1);
        
        return GraphQLError.newError()
            .message(prefix + message)
            .errorType(ErrorType.BAD_REQUEST)
            .path(path)
            .build();
    }
}
