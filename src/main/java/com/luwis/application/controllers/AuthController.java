package com.luwis.application.controllers;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.luwis.application.entities.User;
import com.luwis.application.inputs.LoginInput;
import com.luwis.application.inputs.SignupInput;
import com.luwis.application.middlewares.Validator;
import com.luwis.application.services.AuthService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;
    private final Validator validator;
    
    @MutationMapping
    public User Signup(@Argument SignupInput input) {
        validator.validate(input.email(), input.password());
        return authService.Signup(input.firstName(), input.secondName(), input.email(), input.password());
    }
    
    @QueryMapping
    public String Login(@Argument LoginInput input) {
        validator.validate(input.email(), input.password());
        return authService.Login(input.email(), input.password());
    }

}