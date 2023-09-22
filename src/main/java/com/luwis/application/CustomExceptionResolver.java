package com.luwis.application;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Component;

import com.luwis.application.graphql.resolvers.BadCredentialsResolver;
import com.luwis.application.graphql.resolvers.DataIntegrityResolver;
import com.luwis.application.graphql.resolvers.UsernameNotFoundResolver;

import graphql.GraphQLError;
import graphql.execution.ResultPath;
import graphql.schema.DataFetchingEnvironment;

@Component
public class CustomExceptionResolver extends DataFetcherExceptionResolverAdapter {
    
    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {

        ResultPath path = env.getExecutionStepInfo().getPath();
        
		if (ex instanceof DataIntegrityViolationException) {
            return new DataIntegrityResolver().resolve(path);
        }

        if (ex instanceof UsernameNotFoundException) {
            return new UsernameNotFoundResolver().resolve(path);
        }

        if (ex instanceof BadCredentialsException) {
            return new BadCredentialsResolver().resolve(path);
        }
        
        return null;
	}

}