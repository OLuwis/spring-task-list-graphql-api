package com.luwis.application.todo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.luwis.application.todo.exceptions.InvalidDescriptionException;
import com.luwis.application.todo.exceptions.InvalidTitleException;
import com.luwis.application.todo.exceptions.UnauthorizedException;

@Component
public class TodoUtils {

    @Value("${JWT_SECRET}")
    private String secret;
    
    public String isTitleValid(String title) {
        if (title.length() == 0) throw new InvalidTitleException();

        return title;
    }
    
    public String isDescriptionValid(String description) {
        if (description.length() == 0) throw new InvalidDescriptionException();

        return description;
    }

    public long isTokenValid(String header) {
        boolean hasToken = header.contains("Bearer ") ? true : false;

        if (!hasToken) throw new UnauthorizedException();

        String token = header.split(" ")[1];

        DecodedJWT decoder = JWT
        .require(Algorithm.HMAC256(secret))
        .build()
        .verify(token);

        return decoder.getClaim("id").asLong();
    }
}
