package com.luwis.application.graphql.resolvers;

import org.springframework.graphql.execution.ErrorType;

import graphql.GraphQLError;
import graphql.execution.ResultPath;

public class DataIntegrityResolver extends RuntimeException {
    public GraphQLError resolve(ResultPath path) {
        return GraphQLError.newError()
            .message("Error: Email Is Registered")
            .errorType(ErrorType.BAD_REQUEST)
            .path(path)
            .build();
    }
}