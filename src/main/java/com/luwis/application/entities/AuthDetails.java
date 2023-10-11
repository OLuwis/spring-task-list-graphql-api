package com.luwis.application.entities;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthDetails implements UserDetails {

    private final User user;

    public Long getId() {
        return user.getId();
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String authorities = "READ WRITE UPDATE DELETE";
        return Arrays
            .stream(authorities.split(" "))
            .map(SimpleGrantedAuthority::new)
            .toList();
    }

    @Override
    public String getPassword() {
        if (user == null) throw new EntityNotFoundException("This Email Is Not Registered");
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    
}