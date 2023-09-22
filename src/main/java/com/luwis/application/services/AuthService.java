package com.luwis.application.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.luwis.application.graphql.inputs.LoginInput;
import com.luwis.application.graphql.inputs.SignupInput;
import com.luwis.application.graphql.interfaces.UserImpl;
import com.luwis.application.graphql.types.Login;
import com.luwis.application.graphql.types.Signup;
import com.luwis.application.models.CustomUserDetails;
import com.luwis.application.models.UserModel;
import com.luwis.application.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public Signup signup(SignupInput input) {
        var entity = new UserModel(input.name(), input.email(), passwordEncoder.encode(input.password()));
        var data = userRepository.save(entity);
        var user = new UserImpl(data.getId(), data.getName(), data.getEmail());
        return new Signup(user);
    }

    public Login login(LoginInput input) {
        var authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(input.email(), input.password())
        );
        String token = tokenService.generateToken(authentication);
        var data = (CustomUserDetails) authentication.getPrincipal();
        var user = new UserImpl(data.getId(), data.getName(), data.getUsername());
        return new Login(user, token);
    }

}