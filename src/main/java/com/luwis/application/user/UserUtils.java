package com.luwis.application.user;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class UserUtils {
    public boolean isPasswordValid(String password) {
        boolean valid = true;

        Pattern regex = Pattern.compile("(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[\\W])");
        Matcher matcher = regex.matcher(password);
        
        if (password.length() < 8 
        || password.length() > 20 
        || !matcher.find()) valid = false;

        return valid;
    }

    public boolean isEmailValid(String email) {
        boolean valid = true;

        Pattern regex = Pattern.compile("[a-z0-9]+@[a-z]+\\.[a-z]{2,3}");
        Matcher matcher = regex.matcher(email);

        if (!matcher.matches()) valid = false;

        return valid;        
    }

    public boolean isUsernameValid(String username) {
        boolean valid = true;

        if (username.length() < 3 
        || username.length() > 20) valid = false;

        return valid;
    }
}
