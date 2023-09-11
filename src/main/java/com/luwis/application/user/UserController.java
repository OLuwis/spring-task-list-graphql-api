package com.luwis.application.user;

import org.springframework.stereotype.Controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.luwis.application.user.exceptions.EmailTakenException;
import com.luwis.application.user.exceptions.UserNotFoundException;
import com.luwis.application.user.exceptions.WrongPasswordException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.graphql.data.method.annotation.MutationMapping;

@Controller
public class UserController {
    
    @Autowired
    private UserRepository userRepository;

    @Value("${JWT_SECRET}") private String secret;

    private UserUtils userUtils = new UserUtils();
    private UserService userService = new UserService();
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    
    @MutationMapping
    public UserModel userSignup(@Argument String username, @Argument String email, @Argument String password) {

        UserModel query = userRepository.findByEmail(email);

        if (query != null) throw new EmailTakenException();
        
        UserModel newUser = userService.signup(username, email, password);

        return userRepository.save(newUser);
    }

    @QueryMapping
    public String userLogin(@Argument String email, @Argument String password) {

        userUtils.validateEmail(email);

        UserModel user = userRepository.findByEmail(email);

        if (user == null) throw new UserNotFoundException();

        boolean passwordMatch = encoder.matches(password, user.getPassword());

        if (!passwordMatch) throw new WrongPasswordException();

        Algorithm algorithm = Algorithm.HMAC256(secret);

        String token = JWT.create()
            .withClaim("id", user.getId())
            .withClaim("username", user.getUsername())
            .withIssuer("Luwis")
            .sign(algorithm);

        return token;
        
    }
}
