package com.luwis.application.services;

import java.util.List;
import java.util.Optional;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.luwis.application.entities.Task;
import com.luwis.application.entities.User;
import com.luwis.application.permissions.Delete;
import com.luwis.application.permissions.Read;
import com.luwis.application.permissions.Update;
import com.luwis.application.permissions.Write;
import com.luwis.application.repositories.TaskRepository;
import com.luwis.application.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    
    @Write
    @Override
    public Task CreateTask(String title, Optional<String> description, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AccessDeniedException("Access Denied."));
        Task newTask = new Task();
        newTask.setTitle(title);
        newTask.setDescription(description.orElse(null));
        newTask.setUser(user);
        return taskRepository.save(newTask);
    }

    @Update
    @Override
    public Task UpdateTask(Long id, Optional<String> title, Optional<String> description, Optional<Boolean> pending, Long userId) {
        Task task = taskRepository.findByIdAndUser_id(id, userId).orElseThrow(() -> new EntityNotFoundException("Task Not Found"));
        if (task.getUser().getId() != userId) throw new AccessDeniedException("Access Denied.");
        task.setTitle(title.orElse(task.getTitle()));
        task.setDescription(description.orElse(task.getDescription()));
        task.setPending(pending.orElse(task.getPending()));
        return taskRepository.save(task);
    }

    @Delete
    @Override
    public Task DeleteTask(Long id, Long userId) {
        Task task = taskRepository.findByIdAndUser_id(id, userId).orElseThrow(() -> new EntityNotFoundException("Task Not Found"));
        if (task.getUser().getId() != userId) throw new AccessDeniedException("Access Denied.");
        taskRepository.delete(task);
        return task;
    }

    @Read
    @Override
    public List<Task> GetTasks(Long userId) {
        return taskRepository.findAllByUser_id(userId).orElse(null);
    }
    
}