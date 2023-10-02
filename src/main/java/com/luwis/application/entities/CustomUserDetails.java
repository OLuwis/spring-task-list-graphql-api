package com.luwis.application.entities;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final UserModel user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String authorities = "READ WRITE UPDATE DELETE";
        return Arrays
                .stream(authorities.split(" "))
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    public Long getId() {
        return user.getId();
    }

    public String getName() {
        return user.getName();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public String getPassword() {
        if (user == null) throw new UsernameNotFoundException("Error: User Is Not Registered");
        return user.getPassword();
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