package com.luwis.application.inputs;

import java.util.Optional;

public record CreateTaskInput(String title, Optional<String> description) {}