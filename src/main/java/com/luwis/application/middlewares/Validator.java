package com.luwis.application.middlewares;

import java.security.InvalidParameterException;

import org.apache.commons.validator.routines.EmailValidator;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class Validator {
    
    private final EmailValidator eValidator;
    private final PasswordValidator pValidator;

    public void validate(String email, String password) {
        var passwordData = new PasswordData(password);
        if (!eValidator.isValid(email)) throw new InvalidParameterException("Invalid Email.");
        if (!pValidator.validate(passwordData).isValid()) throw new InvalidParameterException("Invalid Password.");
    }
    
}