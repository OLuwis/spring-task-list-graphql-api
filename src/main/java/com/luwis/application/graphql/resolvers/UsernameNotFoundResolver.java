package com.luwis.application.graphql.resolvers;

import org.springframework.graphql.execution.ErrorType;

import graphql.GraphQLError;
import graphql.execution.ResultPath;

public class UsernameNotFoundResolver extends RuntimeException {
    private final String message = " Error: This user is not registered";

    public GraphQLError resolve(ResultPath path) {
        String prefix = path.toString().substring(1);
        
        return GraphQLError.newError()
            .message(prefix + message)
            .errorType(ErrorType.NOT_FOUND)
            .path(path)
            .build();
    }
}
