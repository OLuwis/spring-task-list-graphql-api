package com.luwis.application.services;

import org.springframework.stereotype.Service;

import com.luwis.application.graphql.inputs.CreateTodoInput;
import com.luwis.application.graphql.responses.CreateTodoRes;
import com.luwis.application.graphql.types.Todo;
import com.luwis.application.models.TodoModel;
import com.luwis.application.repositories.TodoRepository;
import com.luwis.application.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TodoService {
    
    private final UserRepository userRepository;
    private final TodoRepository todoRepository;
    private final TokenService tokenService;
    
    public CreateTodoRes create(CreateTodoInput input) {
        var id = Long.valueOf(tokenService.getSubject());
        var user = userRepository.findById(id);
        var entity = new TodoModel(input.title(), input.description(), false, user.get());
        var todo = todoRepository.save(entity);
        var response = new Todo(todo.getId(), todo.getTitle(), todo.getDescription(), todo.getStatus());
        return new CreateTodoRes(response);
    }

}