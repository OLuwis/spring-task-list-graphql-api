package com.luwis.application.user;

import org.springframework.stereotype.Controller;

import com.luwis.application.user.exceptions.EmailTakenException;
import com.luwis.application.user.exceptions.UserNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;

@Controller
public class UserController {
    
    @Autowired
    private UserRepository userRepository;
    
    private UserUtils userUtils = new UserUtils();
    private UserService userService = new UserService();
    
    @MutationMapping
    public UserModel userSignup(@Argument String username, @Argument String email, @Argument String password) {

        UserModel query = userRepository.findByEmail(email);

        if (query != null) throw new EmailTakenException();
        
        UserModel newUser = userService.signup(username, email, password);

        return userRepository.save(newUser);
    }

    @QueryMapping
    public String userLogin(@Argument String email, @Argument String password) {

        userUtils.validateEmail(email);

        UserModel user = userRepository.findByEmail(email);

        if (user == null) throw new UserNotFoundException();

        return userService.login(user, password);
        
    }
}
