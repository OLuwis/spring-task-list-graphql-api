package com.luwis.application;

import graphql.GraphQLError;
import graphql.schema.DataFetchingEnvironment;

import org.springframework.stereotype.Component;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;

import com.luwis.application.user.exceptions.EmailTakenException;
import com.luwis.application.user.exceptions.InvalidEmailException;
import com.luwis.application.user.exceptions.InvalidPasswordException;
import com.luwis.application.user.exceptions.InvalidUsernameException;
import com.luwis.application.user.exceptions.UserNotFoundException;
import com.luwis.application.user.exceptions.WrongPasswordException;

@Component
public class GraphQLExceptionHandler extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {

        if (ex instanceof InvalidPasswordException) return new InvalidPasswordException().error();

        if (ex instanceof InvalidEmailException) return new InvalidEmailException().error();

        if (ex instanceof InvalidUsernameException) return new InvalidUsernameException().error();

        if (ex instanceof EmailTakenException) return new EmailTakenException().error();

        if (ex instanceof UserNotFoundException) return new UserNotFoundException().error();

        if (ex instanceof WrongPasswordException) return new WrongPasswordException().error();
        
        return null;

    }
}
