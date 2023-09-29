package com.luwis.application.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.luwis.application.models.TodoModel;

@Repository
public interface TodoRepository extends CrudRepository<TodoModel, Long> {
    List<TodoModel> findAllByUser_id(Long id);
}