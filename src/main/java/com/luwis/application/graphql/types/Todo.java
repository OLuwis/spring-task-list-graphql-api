package com.luwis.application.graphql.types;

public record Todo(Long id, String title, String description, Boolean status) {}