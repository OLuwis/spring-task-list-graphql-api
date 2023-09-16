package com.luwis.application.user;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.luwis.application.user.exceptions.UserRegisteredException;
import com.luwis.application.user.exceptions.WrongPasswordException;
import com.luwis.application.user.exceptions.InvalidEmailException;
import com.luwis.application.user.exceptions.InvalidPasswordException;
import com.luwis.application.user.exceptions.InvalidUsernameException;
import com.luwis.application.user.exceptions.UserNotFoundException;

@Component
public class UserUtils {
    @Autowired
    private UserRepository userRepository;

    public String isPasswordValid(String password) {
        Pattern regex = Pattern.compile("(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[\\W])");
        Matcher matcher = regex.matcher(password);
        
        if (password.length() < 8 
        || password.length() > 20 
        || !matcher.find()) throw new InvalidPasswordException();

        return password;
    }

    public String isEmailValid(String email) {
        Pattern regex = Pattern.compile("[a-z0-9]+@[a-z]+\\.[a-z]{2,3}");
        Matcher matcher = regex.matcher(email);

        if (!matcher.matches()) throw new InvalidEmailException();

        return email;        
    }

    public String isUsernameValid(String username) {
        if (username.length() < 3 
        || username.length() > 20) throw new InvalidUsernameException();

        return username;
    }

    public UserModel isUserRegistered(String email, boolean flag) {
        Optional<UserModel> user = userRepository.findByEmail(email);

        if (flag) {
            if (user.isEmpty()) throw new UserNotFoundException();
            return user.get();
        }

        if (user.isPresent()) throw new UserRegisteredException();
        
        return null;
    }

    public void isPasswordCorrect(String password, String userPassword) {
        boolean match = new BCryptPasswordEncoder().matches(password, userPassword);

        if (!match) throw new WrongPasswordException();
    }

}