package com.luwis.application.graphql.types;

import com.luwis.application.graphql.interfaces.UserImpl;

public record Login(UserImpl user, String token) {}