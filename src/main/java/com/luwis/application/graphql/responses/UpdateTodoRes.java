package com.luwis.application.graphql.responses;

import com.luwis.application.graphql.types.Todo;

public record UpdateTodoRes(Todo before, Todo after) {}