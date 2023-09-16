package com.luwis.application.todo;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.luwis.application.todo.exceptions.TodoNotFoundException;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private TodoUtils todoUtils;

    @Value("${JWT_SECRET}")
    private String secret;
    
    public TodoModel createTodo(String title, String description, String header) {
        title = todoUtils.isTitleValid(title);

        description = todoUtils.isDescriptionValid(description);

        long userid = todoUtils.isTokenValid(header);

        TodoModel newTodo = new TodoModel(title, description, userid);

        return todoRepository.save(newTodo);
    }

    public List<TodoModel> getTodos(String header) {
        long userid = todoUtils.isTokenValid(header);

        return todoRepository.findAllByUserid(userid);
    }
    
    public String deleteTodo(long id, String header) {
        long userid = todoUtils.isTokenValid(header);

        Optional<TodoModel> todo = todoRepository.findByIdAndUserid(id, userid);

        if (todo.isEmpty()) throw new TodoNotFoundException();

        todoRepository.deleteById(id);

        return "Todo Deleted!";
    }

    public TodoModel updateTodo(Optional<String> title, Optional<String> description, Optional<Boolean> status, long id, String header) {
        long userid = todoUtils.isTokenValid(header);

        Optional<TodoModel> todo = todoRepository.findByIdAndUserid(id, userid);

        if (todo.isEmpty()) throw new TodoNotFoundException();

        TodoModel newTodo = todo.get();

        String newTitle = newTodo.getTitle();
        String newDesc = newTodo.getDescription();
        boolean newStatus = newTodo.getStatus();

        if (title.isPresent()) newTitle = todoUtils.isTitleValid(title.get());

        if (description.isPresent()) newDesc = todoUtils.isDescriptionValid(description.get());

        if (status.isPresent()) newStatus = status.get();

        newTodo.setTitle(newTitle);
        newTodo.setDescription(newDesc);
        newTodo.setStatus(newStatus);
        
        return todoRepository.save(newTodo);
    }

}