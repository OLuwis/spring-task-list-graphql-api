package com.luwis.application.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.luwis.application.user.exceptions.EmailTakenException;
import com.luwis.application.user.exceptions.InvalidEmailException;
import com.luwis.application.user.exceptions.InvalidPasswordException;
import com.luwis.application.user.exceptions.InvalidUsernameException;
import com.luwis.application.user.exceptions.UserNotFoundException;
import com.luwis.application.user.exceptions.WrongPasswordException;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserUtils userUtils;

    @Value("${JWT_SECRET}")
    private String secret;
    
    public UserModel signup(String username, String email, String password) {
        UserModel user = userRepository.findByEmail(email);

        if (user != null) throw new EmailTakenException();

        if (!userUtils.isPasswordValid(password)) throw new InvalidPasswordException();
        if (!userUtils.isEmailValid(email)) throw new InvalidEmailException();
        if (!userUtils.isUsernameValid(username)) throw new InvalidUsernameException();

        UserModel newUser = new UserModel(username, email, new BCryptPasswordEncoder().encode(password));

        return userRepository.save(newUser);
    }

    public String login(String email, String password) {
        if (!userUtils.isEmailValid(email)) throw new InvalidEmailException();

        UserModel user = userRepository.findByEmail(email);

        if (user == null) throw new UserNotFoundException();

        boolean isPasswordCorrect = new BCryptPasswordEncoder().matches(password, user.getPassword());

        if (!isPasswordCorrect) throw new WrongPasswordException();

        return JWT.create()
        .withClaim("id", user.getId())
        .withClaim("username", user.getUsername())
        .withIssuer("Luwis")
        .sign(Algorithm.HMAC256(secret));
    }

}
