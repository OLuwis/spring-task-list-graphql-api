package com.luwis.application.todo;

import org.springframework.stereotype.Component;

@Component
public class TodoUtils {
    public boolean isTitleValid(String title) {
        boolean valid = true;

        if (title.length() == 0) valid = false;

        return valid;
    }
    
    public boolean isDescriptionValid(String description) {
        boolean valid = true;

        if (description.length() == 0) valid = false;

        return valid;
    }
}
