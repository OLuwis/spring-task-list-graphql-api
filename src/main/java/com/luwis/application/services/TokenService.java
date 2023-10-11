package com.luwis.application.services;

import org.springframework.security.core.Authentication;

public interface TokenService {
    public String generateToken(Authentication authentication);
    public String extractSubject();
}