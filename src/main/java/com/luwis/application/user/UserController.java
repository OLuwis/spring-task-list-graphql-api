package com.luwis.application.user;

import org.springframework.stereotype.Controller;

import com.luwis.application.user.exceptions.EmailException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;
    
    @MutationMapping
    public UserModel userSignup(@Argument String username, @Argument String email, @Argument String password) {
        UserService userService = new UserService();
        userService.signup(username, email, password);
        UserModel query = userRepository.findByEmail(email);
        if (query != null) throw new EmailException("Invalid Email: Email Is Already In Use");
        UserModel newUser = new UserModel(username, email, password);
        return userRepository.save(newUser);

    }

}
