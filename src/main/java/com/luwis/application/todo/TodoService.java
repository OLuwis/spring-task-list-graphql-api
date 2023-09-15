package com.luwis.application.todo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.luwis.application.todo.exceptions.InvalidDescriptionException;
import com.luwis.application.todo.exceptions.InvalidTitleException;
import com.luwis.application.todo.exceptions.UnauthorizedException;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private TodoUtils todoUtils;

    @Value("${JWT_SECRET}")
    private String secret;
    
    public TodoModel createTodo(String title, String description, String header) {
        boolean hasToken = header.contains("Bearer ") ? true : false;

        if (!todoUtils.isTitleValid(title)) throw new InvalidTitleException();

        if (!todoUtils.isTitleValid(description)) throw new InvalidDescriptionException();

        if (!hasToken) throw new UnauthorizedException();

        String token = header.split(" ")[1];

        DecodedJWT decoder = JWT
        .require(Algorithm.HMAC256(secret))
        .build()
        .verify(token);

        Long userid = decoder.getClaim("id").asLong();

        TodoModel newTodo = new TodoModel(title, description, userid);

        return todoRepository.save(newTodo);
    }

    public List<TodoModel> getTodos(String header) {
        boolean hasToken = header.contains("Bearer ") ? true : false;

        if (!hasToken) throw new UnauthorizedException();

        String token = header.split(" ")[1];

        DecodedJWT decoder = JWT
        .require(Algorithm.HMAC256(secret))
        .build()
        .verify(token);

        Long userid = decoder.getClaim("id").asLong();

        return todoRepository.findAllByUserid(userid);
    }
    
}
