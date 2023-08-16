package com.luwis.application;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;
import org.springframework.graphql.execution.ErrorType;
import com.luwis.application.user.exceptions.EmailException;
import com.luwis.application.user.exceptions.PasswordException;
import com.luwis.application.user.exceptions.UsernameException;

import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;

@Component
public class GraphQLExceptionsHandler extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {

        if (ex instanceof PasswordException) {
            return GraphqlErrorBuilder.newError()
            .errorType(ErrorType.BAD_REQUEST)
            .message("Invalid Password: Passwords Must Be Between 8-20 Characters Long And Contain A Number, An Upper And Lowercase Letter, And A Special Symbol")
            .path(env.getExecutionStepInfo().getPath())
            .location(env.getField().getSourceLocation())
            .build();
        }

        if (ex instanceof EmailException) {
            return GraphqlErrorBuilder.newError()
            .errorType(ErrorType.INTERNAL_ERROR)
            .message(ex.getMessage())
            .path(env.getExecutionStepInfo().getPath())
            .location(env.getField().getSourceLocation())
            .build();
        }

        if (ex instanceof UsernameException) {
            return GraphqlErrorBuilder.newError()
            .errorType(ErrorType.BAD_REQUEST)
            .message("Invalid Username: Usernames Must Be Between 3-20 Characters Long")
            .path(env.getExecutionStepInfo().getPath())
            .location(env.getField().getSourceLocation())
            .build();
        }

        return null;

    }
}
