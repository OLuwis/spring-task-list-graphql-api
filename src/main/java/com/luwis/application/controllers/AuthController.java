package com.luwis.application.controllers;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.luwis.application.graphql.inputs.LoginInput;
import com.luwis.application.graphql.inputs.SignupInput;
import com.luwis.application.graphql.types.Login;
import com.luwis.application.graphql.types.Signup;
import com.luwis.application.services.AuthService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;
    
    @MutationMapping
    public Signup SignupUser(@Argument SignupInput user) {
        return authService.signup(user);
    }
    
    @QueryMapping
    public Login LoginUser(@Argument LoginInput user) {
        return authService.login(user);
    }

}