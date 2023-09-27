package com.luwis.application.services;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.luwis.application.graphql.inputs.CreateTodoInput;
import com.luwis.application.graphql.inputs.DeleteTodoInput;
import com.luwis.application.graphql.responses.CreateTodoRes;
import com.luwis.application.graphql.responses.DeleteTodoRes;
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
        if (user.isEmpty()) throw new AccessDeniedException(null);
        var entity = new TodoModel(input.title(), input.description(), false, user.get());
        var todo = todoRepository.save(entity);
        var response = new Todo(todo.getId(), todo.getTitle(), todo.getDescription(), todo.getStatus());
        return new CreateTodoRes(response);
    }

    public DeleteTodoRes delete(DeleteTodoInput input) {
        var todo = todoRepository.findById(input.id()).get();
        todoRepository.delete(todo);
        var response = new Todo(todo.getId(), todo.getTitle(), todo.getDescription(), todo.getStatus());
        return new DeleteTodoRes(response);
    }
}