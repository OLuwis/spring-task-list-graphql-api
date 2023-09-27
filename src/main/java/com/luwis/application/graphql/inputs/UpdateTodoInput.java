package com.luwis.application.graphql.inputs;

import java.util.Optional;

public record UpdateTodoInput(Long id, Optional<String> title, Optional<String> description, Optional<Boolean> status) {}