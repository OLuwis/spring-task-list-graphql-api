package com.luwis.application.controllers;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.luwis.application.graphql.inputs.LoginInput;
import com.luwis.application.graphql.inputs.SignupInput;
import com.luwis.application.graphql.responses.LoginRes;
import com.luwis.application.graphql.responses.SignupRes;
import com.luwis.application.services.AuthService;
import com.luwis.application.utils.InputValidator;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AuthController {
    
    private final InputValidator inputValidator;
    private final AuthService authService;

    @MutationMapping
    public SignupRes Signup(@Argument SignupInput user) {
        inputValidator.validate(user);
        return authService.signup(user);
    }
    
    @QueryMapping
    public LoginRes Login(@Argument LoginInput user) {
        inputValidator.validate(user);
        return authService.login(user);
    }

}