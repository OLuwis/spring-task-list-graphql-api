package com.luwis.application.graphql.interfaces;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserImpl implements User {
    public Long id;
    public String name;
    public String email;
}