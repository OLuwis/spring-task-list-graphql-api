package com.luwis.application.graphql.resolvers;

import org.springframework.graphql.execution.ErrorType;

import graphql.GraphQLError;
import graphql.execution.ResultPath;

public class DataIntegrityResolver extends RuntimeException {
    private final String message = " Error: This Email Is Registered";
    
    public GraphQLError resolve(ResultPath path) {
        return GraphQLError.newError()
            .message(message)
            .errorType(ErrorType.BAD_REQUEST)
            .path(path)
            .build();
    }
}