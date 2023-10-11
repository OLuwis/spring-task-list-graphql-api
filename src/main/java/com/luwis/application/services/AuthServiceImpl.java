package com.luwis.application.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.luwis.application.entities.User;
import com.luwis.application.repositories.UserRepository;

import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public User Signup(String firstName, String secondName, String email, String password) {
        userRepository.findByEmail(email)
        .ifPresent(u -> {throw new EntityExistsException("Email Is Already Registered");});
        User newUser = new User();
        newUser.setFirstName(firstName);
        newUser.setSecondName(secondName);
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));
        return userRepository.save(newUser);
    }

    @Override
    public String Login(String email, String password) {
        var newAuth = new UsernamePasswordAuthenticationToken(email, password);
        var auth = authenticationManager.authenticate(newAuth);
        return tokenService.generateToken(auth);
    }
    
}