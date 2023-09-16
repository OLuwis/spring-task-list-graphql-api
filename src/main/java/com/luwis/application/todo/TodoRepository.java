package com.luwis.application.todo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends CrudRepository<TodoModel, Long> {
    List<TodoModel> findAllByUserid(long userid);
    Optional<TodoModel> findByIdAndUserid(long id, long userid);
}
