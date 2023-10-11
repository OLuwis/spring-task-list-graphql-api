package com.luwis.application.services;

import com.luwis.application.entities.User;

public interface AuthService {
    public User Signup(String firstName, String secondName, String email, String password);
    public String Login(String email, String password);
}