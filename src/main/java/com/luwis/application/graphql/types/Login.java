package com.luwis.application.graphql.types;

import com.luwis.application.graphql.interfaces.User;

public record Login(User user, String token) {}