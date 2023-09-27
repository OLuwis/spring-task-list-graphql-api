package com.luwis.application.graphql.resolvers;

import org.springframework.graphql.execution.ErrorType;

import graphql.GraphQLError;
import graphql.execution.ResultPath;

public class AccessDeniedResolver extends RuntimeException {
    private final String message = "Error: Access Denied";
    
    public GraphQLError resolve(ResultPath path) {
        return GraphQLError.newError()
            .message(message)
            .errorType(ErrorType.UNAUTHORIZED)
            .path(path)
            .build();
    }
}