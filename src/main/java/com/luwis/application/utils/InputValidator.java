package com.luwis.application.utils;

import org.apache.commons.validator.routines.EmailValidator;
import org.passay.PasswordData;
import org.passay.PasswordValidator;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import com.luwis.application.graphql.inputs.LoginInput;
import com.luwis.application.graphql.inputs.SignupInput;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InputValidator {
    
    private final EmailValidator emailValidator;
    private final PasswordValidator passValidator;
    
    public void validate(SignupInput input) {
        final var email = input.email();
        final var password = new PasswordData(input.password());

        if (!emailValidator.isValid(email)) 
        throw new BadCredentialsException(" Error: Invalid Email");

        if (!passValidator.validate(password).isValid()) 
        throw new BadCredentialsException(" Error: Invalid Password");
    }

    public void validate(LoginInput input) {
        final var email = input.email();
        final var password = new PasswordData(input.password());

        if (!emailValidator.isValid(email)) 
        throw new BadCredentialsException(" Error: Invalid Email");
        
        if (!passValidator.validate(password).isValid()) 
        throw new BadCredentialsException(" Error: Invalid Password");
    }

}