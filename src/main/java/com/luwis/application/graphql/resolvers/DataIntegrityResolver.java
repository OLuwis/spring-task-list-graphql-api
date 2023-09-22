package com.luwis.application.graphql.resolvers;

import org.springframework.graphql.execution.ErrorType;

import graphql.GraphQLError;
import graphql.execution.ResultPath;

public class DataIntegrityResolver extends RuntimeException {
    private final String message = " Error: This user is already registered.";
    
    public GraphQLError resolve(ResultPath path) {
        String prefix = path.toString().substring(1);

        return GraphQLError.newError()
            .message(prefix + message)
            .errorType(ErrorType.BAD_REQUEST)
            .path(path)
            .build();
    }
}
