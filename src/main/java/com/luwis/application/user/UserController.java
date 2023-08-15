package com.luwis.application.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;
    
    @MutationMapping
    public UserModel userSignup(@Argument String username, @Argument String email, @Argument String password) {
        UserModel newUser = new UserModel(username, email, password);
        return userRepository.save(newUser);
    }

}
