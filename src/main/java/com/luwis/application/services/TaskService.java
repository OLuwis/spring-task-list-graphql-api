package com.luwis.application.services;

import java.util.List;
import java.util.Optional;

import com.luwis.application.entities.Task;

public interface TaskService {
    public Task CreateTask(String title, Optional<String> description, Long userId);
    public Task UpdateTask(Long id, Optional<String> title, Optional<String> description, Optional<Boolean> pending, Long userId);
    public Task DeleteTask(Long id, Long userId);
    public List<Task> GetTasks(Long userId);
}