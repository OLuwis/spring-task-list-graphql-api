package com.luwis.application;

import java.security.InvalidParameterException;

import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.stereotype.Component;

import graphql.GraphQLError;
import graphql.execution.ResultPath;
import graphql.schema.DataFetchingEnvironment;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

@Component
public class ExceptionResolver extends DataFetcherExceptionResolverAdapter {
    
    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        
        ResultPath path = env.getExecutionStepInfo().getPath();
        String msg = ex.getMessage();

        if (ex instanceof InvalidParameterException) {
            return new Exception().resolve(msg, path);
        }

        if (ex instanceof EntityExistsException) {
            return new Exception().resolve(msg, path);
        }
        
        if (ex instanceof EntityNotFoundException) {
            return new Exception().resolve(msg, path);
        }
        
        return null;
	}

}