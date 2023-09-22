package com.luwis.application.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;

import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import com.luwis.application.models.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService {
    
    private final JwtEncoder jwtEncoder;

    public String generateToken(Authentication authentication) {
        var user = (CustomUserDetails) authentication.getPrincipal();
        Instant now = Instant.now();
        String scope = authentication
            .getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(" "));
        var claims = JwtClaimsSet
            .builder()
            .issuer("self")
            .issuedAt(now)
            .expiresAt(now.plus(24, ChronoUnit.HOURS))
            .subject(user.getId().toString())
            .claim("scope", scope)
            .build();
        var encoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);
        return this.jwtEncoder.encode(encoderParameters).getTokenValue();
    }

    public String getSubject() {
        var principal = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Jwt token = principal.getToken();
        return token.getSubject();
    }

    public String getClaim(String claim) {
        var principal = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Jwt token = principal.getToken();
        return token.getClaim(claim);
    }

}