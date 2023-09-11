package com.luwis.application.user;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.luwis.application.user.exceptions.InvalidEmailException;
import com.luwis.application.user.exceptions.InvalidPasswordException;
import com.luwis.application.user.exceptions.InvalidUsernameException;

public class UserUtils {

    public void validatePassword(String password) {

        Pattern regex = Pattern.compile("(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[\\W])");
        Matcher matcher = regex.matcher(password);
        
        if (password.length() < 8) throw new InvalidPasswordException();
        if (password.length() > 20) throw new InvalidPasswordException();
        if (!matcher.find()) throw new InvalidPasswordException();

    }

    public void validateEmail(String email) {

        Pattern regex = Pattern.compile("[a-z0-9]+@[a-z]+\\.[a-z]{2,3}");
        Matcher matcher = regex.matcher(email);

        if (!matcher.matches()) throw new InvalidEmailException();
        
    }

    public void validateUsername(String username) {

        if (username.length() > 20) throw new InvalidUsernameException();
        if (username.length() < 3) throw new InvalidUsernameException();
        
    }
    
}
