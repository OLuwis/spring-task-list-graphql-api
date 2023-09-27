package com.luwis.application;

import java.util.NoSuchElementException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.luwis.application.graphql.resolvers.AccessDeniedResolver;
import com.luwis.application.graphql.resolvers.BadCredentialsResolver;
import com.luwis.application.graphql.resolvers.DataIntegrityResolver;
import com.luwis.application.graphql.resolvers.NotFoundResolver;

import graphql.GraphQLError;
import graphql.execution.ResultPath;
import graphql.schema.DataFetchingEnvironment;

@Component
public class CustomExceptionResolver extends DataFetcherExceptionResolverAdapter {
    
    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        
        ResultPath path = env.getExecutionStepInfo().getPath();
        String message = ex.getMessage();
        
		if (ex instanceof DataIntegrityViolationException) {
            return new DataIntegrityResolver().resolve(path);
        }

        if (ex instanceof UsernameNotFoundException) {
            return new NotFoundResolver().resolve(message, path);
        }

        if (ex instanceof BadCredentialsException) {
            return new BadCredentialsResolver().resolve(message, path);
        }

        if (ex instanceof AccessDeniedException) {
            return new AccessDeniedResolver().resolve(path);
        }

        if (ex instanceof NoSuchElementException) {
            return new NotFoundResolver().resolve("Error: Resource Not Found", path);
        }
        
        return null;
	}

}