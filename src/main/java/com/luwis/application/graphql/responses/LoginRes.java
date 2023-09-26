package com.luwis.application.graphql.responses;

import com.luwis.application.graphql.types.User;

public record LoginRes(User user, String token) {}