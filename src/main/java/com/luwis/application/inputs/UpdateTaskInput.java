package com.luwis.application.inputs;

import java.util.Optional;

public record UpdateTaskInput(Long id, Optional<String> title, Optional<String> description, Optional<Boolean> pending) {}