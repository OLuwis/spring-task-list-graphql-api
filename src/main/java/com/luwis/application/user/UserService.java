package com.luwis.application.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.luwis.application.user.exceptions.WrongPasswordException;

@Service
public class UserService {

    private UserUtils userUtils = new UserUtils();
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private JwtEncoderParameters jwtEncoder; 
    
    public UserModel signup(String username, String email, String password) {

        userUtils.validatePassword(password);
        userUtils.validateEmail(email);
        userUtils.validateUsername(username);

        String hash = encoder.encode(password);
        
        return new UserModel(username, email, hash);

    }

    public String login(UserModel user, String password) {

        boolean passwordMatch = encoder.matches(password, user.getPassword());

        if (!passwordMatch) throw new WrongPasswordException();
        
        
        
        return null;

    }

}
