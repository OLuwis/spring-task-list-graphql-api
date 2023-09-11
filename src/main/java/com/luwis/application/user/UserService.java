package com.luwis.application.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserService {
    
    private UserUtils userUtils = new UserUtils();
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    
    public UserModel signup(String username, String email, String password) {

        userUtils.validatePassword(password);
        userUtils.validateEmail(email);
        userUtils.validateUsername(username);

        String hash = encoder.encode(password);
        
        return new UserModel(username, email, hash);

    }

}
