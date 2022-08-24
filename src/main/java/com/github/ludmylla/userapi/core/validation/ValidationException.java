package com.github.ludmylla.userapi.core.validation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.BindingResult;

@AllArgsConstructor
@Getter
public class ValidationException extends RuntimeException{

    private static final long serialVersionUID = 1L;
    private BindingResult bindingResult;
}
