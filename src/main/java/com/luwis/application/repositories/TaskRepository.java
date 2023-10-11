package com.luwis.application.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.luwis.application.entities.Task;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {
    Optional<Task> findByIdAndUser_id(Long id, Long userId);
    Optional<List<Task>> findAllByUser_id(Long userId);
}