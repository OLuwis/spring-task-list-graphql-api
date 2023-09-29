package com.luwis.application.graphql.responses;

import java.util.List;

import com.luwis.application.graphql.types.Todo;

public record GetTodosRes(List<Todo> todos) {}