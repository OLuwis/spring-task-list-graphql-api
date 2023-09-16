package com.luwis.application.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserUtils userUtils;

    @Value("${JWT_SECRET}")
    private String secret;
    
    public UserModel signup(String username, String email, String password) {
        email = userUtils.isEmailValid(email);

        userUtils.isUserRegistered(email, false);

        password = userUtils.isPasswordValid(password);

        username = userUtils.isUsernameValid(username);

        UserModel newUser = new UserModel(username, email, new BCryptPasswordEncoder().encode(password));

        return userRepository.save(newUser);
    }

    public String login(String email, String password) {
        email = userUtils.isEmailValid(email);

        UserModel user = userUtils.isUserRegistered(email, true);

        userUtils.isPasswordCorrect(password, user.getPassword());
        
        return JWT.create()
        .withClaim("id", user.getId())
        .withClaim("username", user.getUsername())
        .withIssuer("Luwis")
        .sign(Algorithm.HMAC256(secret));
    }

}
