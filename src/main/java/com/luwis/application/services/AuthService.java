package com.luwis.application.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.luwis.application.entities.CustomUserDetails;
import com.luwis.application.entities.Task;
import com.luwis.application.entities.UserModel;
import com.luwis.application.graphql.inputs.LoginInput;
import com.luwis.application.graphql.inputs.SignupInput;
import com.luwis.application.graphql.responses.LoginRes;
import com.luwis.application.graphql.responses.SignupRes;
import com.luwis.application.graphql.types.User;
import com.luwis.application.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public SignupRes signup(SignupInput input) throws BadCredentialsException {
        final var entity = new UserModel(input.name(), input.email(), passwordEncoder.encode(input.password()));
        final var data = userRepository.save(entity);
        final User user = new User(data.getId(), data.getName(), data.getEmail());
        return new SignupRes(user);
    }

    public LoginRes login(LoginInput input) {
        final var auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(input.email(), input.password()));
        final String token = tokenService.generateToken(auth);
        final var data = (CustomUserDetails) auth.getPrincipal();
        final User user = new User(data.getId(), data.getName(), data.getUsername());
        return new LoginRes(user, token);
    }

}