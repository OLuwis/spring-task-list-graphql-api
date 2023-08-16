package com.luwis.application.user;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.luwis.application.user.exceptions.EmailException;
import com.luwis.application.user.exceptions.PasswordException;
import com.luwis.application.user.exceptions.UsernameException;

public class UserService {

    public void signup(String username, String email, String password) {

        Pattern passwordRegex = Pattern.compile("[\\w\\W]");
        Pattern emailRegex = Pattern.compile("[a-z0-9]+@[a-z]+\\.[a-z]{2,3}");
        Matcher passwordMatcher = passwordRegex.matcher(password);
        Matcher emailMatcher = emailRegex.matcher(email);
        Integer matches = 0;
        while (passwordMatcher.find()) {
            matches++;
        }
        if (password.length() < 8 || password.length() > 20 || password.length() != matches) throw new PasswordException();
        if (!emailMatcher.matches()) throw new EmailException("Invalid Email: Please Put A Valid Email");
        if (username.length() < 3 || username.length() > 20) throw new UsernameException();
        
    }

}
