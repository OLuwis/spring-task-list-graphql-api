package com.luwis.application;

import java.util.Collections;

import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;

import reactor.core.publisher.Mono;

public class RequestHeaderInterceptor implements WebGraphQlInterceptor {

    @Override
    public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
        String header = request.getHeaders().getFirst("Authorization");
        String value = (header != null) ? header : "";
        request.configureExecutionInput((executionInput, builder) -> {
            return builder.graphQLContext(Collections.singletonMap("authHeader", value)).build();
        });
        return chain.next(request);
    }
    
}