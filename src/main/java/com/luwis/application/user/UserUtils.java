package com.luwis.application.user;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.web.bind.annotation.ControllerAdvice;

import com.luwis.application.user.exceptions.InvalidEmailException;
import com.luwis.application.user.exceptions.InvalidPasswordException;
import com.luwis.application.user.exceptions.InvalidUsernameException;

@ControllerAdvice
public class UserUtils {

    public void validatePassword(String password) {

        InvalidPasswordException error = new InvalidPasswordException();

        Pattern regex = Pattern.compile("(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[\\W])");
        Matcher matcher = regex.matcher(password);
        
        if (password.length() < 8) throw error;
        if (password.length() > 20) throw error;
        if (!matcher.find()) throw error;

    }

    public void validateEmail(String email) {

        Pattern regex = Pattern.compile("[a-z0-9]+@[a-z]+\\.[a-z]{2,3}");
        Matcher matcher = regex.matcher(email);

        if (!matcher.matches()) throw new InvalidEmailException();
        
    }

    public void validateUsername(String username) {

        InvalidUsernameException error = new InvalidUsernameException();

        if (username.length() > 20) throw error;
        if (username.length() < 3) throw error;
        
    }
    
}
