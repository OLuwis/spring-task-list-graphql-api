package com.luwis.application;

import graphql.GraphQLError;
import graphql.schema.DataFetchingEnvironment;

import org.springframework.stereotype.Component;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;

import com.luwis.application.todo.exceptions.InvalidDescriptionException;
import com.luwis.application.todo.exceptions.InvalidTitleException;
import com.luwis.application.todo.exceptions.TodoNotFoundException;
import com.luwis.application.todo.exceptions.UnauthorizedException;
import com.luwis.application.user.exceptions.UserRegisteredException;
import com.luwis.application.user.exceptions.InvalidEmailException;
import com.luwis.application.user.exceptions.InvalidPasswordException;
import com.luwis.application.user.exceptions.InvalidUsernameException;
import com.luwis.application.user.exceptions.UserNotFoundException;
import com.luwis.application.user.exceptions.WrongPasswordException;

@Component
public class ExceptionHandler extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {        
        if (ex instanceof InvalidPasswordException) return new InvalidPasswordException().error();

        if (ex instanceof InvalidEmailException) return new InvalidEmailException().error();

        if (ex instanceof InvalidUsernameException) return new InvalidUsernameException().error();

        if (ex instanceof UserRegisteredException) return new UserRegisteredException().error();

        if (ex instanceof UserNotFoundException) return new UserNotFoundException().error();

        if (ex instanceof WrongPasswordException) return new WrongPasswordException().error();

        if (ex instanceof UnauthorizedException) return new UnauthorizedException().error();

        if (ex instanceof InvalidTitleException) return new InvalidTitleException().error();

        if (ex instanceof InvalidDescriptionException) return new InvalidDescriptionException().error();
        
        if (ex instanceof TodoNotFoundException) return new TodoNotFoundException().error();
        
        return null;
    }
}
