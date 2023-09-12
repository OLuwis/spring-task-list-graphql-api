package com.luwis.application.user;

import org.springframework.stereotype.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @MutationMapping
    public UserModel userSignup(@Argument String username, @Argument String email, @Argument String password) {
        return userService.signup(username, email, password);
    }

    @QueryMapping
    public String userLogin(@Argument String email, @Argument String password) {
        return userService.login(email, password);
    }
}
