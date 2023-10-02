package com.luwis.application.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.luwis.application.entities.User;

@Repository
public interface UserRepo extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);
}