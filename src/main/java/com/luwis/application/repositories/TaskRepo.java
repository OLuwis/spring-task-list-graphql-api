package com.luwis.application.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.luwis.application.entities.Task;

@Repository
public interface TaskRepo extends CrudRepository<Task, Long> {
    Optional<Task> findByIdAndUser_id(Long id, Long userId);
    Optional<Task> deleteByIdAndUser_id(Long id, Long userId);
}