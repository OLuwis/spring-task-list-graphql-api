package com.luwis.application;

import graphql.GraphQLError;
import graphql.execution.ResultPath;

public class Exception extends RuntimeException {
    public GraphQLError resolve(String msg, ResultPath path) {
        return GraphQLError.newError()
            .message(msg)
            .path(path)
            .build();
    }
}