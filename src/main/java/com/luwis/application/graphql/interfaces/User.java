package com.luwis.application.graphql.interfaces;

public record User(Long id, String name, String email) implements UserInterface {}