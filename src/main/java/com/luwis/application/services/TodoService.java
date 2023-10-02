package com.luwis.application.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.luwis.application.entities.TodoModel;
import com.luwis.application.graphql.inputs.CreateTodoInput;
import com.luwis.application.graphql.inputs.DeleteTodoInput;
import com.luwis.application.graphql.inputs.UpdateTodoInput;
import com.luwis.application.graphql.responses.CreateTodoRes;
import com.luwis.application.graphql.responses.DeleteTodoRes;
import com.luwis.application.graphql.responses.GetTodosRes;
import com.luwis.application.graphql.responses.UpdateTodoRes;
import com.luwis.application.graphql.types.Todo;
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
        var id = Long.valueOf(tokenService.getSubject());

        var todo = todoRepository.findById(input.id()).get();
        if (!todo.getUser().getId().equals(id)) throw new AccessDeniedException(null);

        todoRepository.delete(todo);

        var response = new Todo(todo.getId(), todo.getTitle(), todo.getDescription(), todo.getStatus());

        return new DeleteTodoRes(response);
    }

    public UpdateTodoRes update(UpdateTodoInput input) {
        var id = Long.valueOf(tokenService.getSubject());
        
        var todo = todoRepository.findById(input.id()).get();
        if (!todo.getUser().getId().equals(id)) throw new AccessDeniedException(null);

        var before = new Todo(todo.getId(), todo.getTitle(), todo.getDescription(), todo.getStatus());

        todo.setTitle(input.title().isPresent() ? input.title().get() : todo.getTitle());
        todo.setDescription(input.description().isPresent() ? input.description().get() : todo.getDescription());
        todo.setStatus(input.status().isPresent() ? input.status().get() : todo.getStatus());

        todoRepository.save(todo);

        var after = new Todo(todo.getId(), todo.getTitle(), todo.getDescription(), todo.getStatus());
        
        return new UpdateTodoRes(before, after);
    }

    public GetTodosRes get() {
        var id = Long.valueOf(tokenService.getSubject());
        
        var todos = todoRepository.findAllByUser_id(id);

        List<Todo> response = todos
            .stream()
            .map(todo -> {
                return new Todo(todo.getId(), todo.getTitle(), todo.getDescription(), todo.getStatus());})
            .collect(Collectors.toList());

        return new GetTodosRes(response);
    }
}