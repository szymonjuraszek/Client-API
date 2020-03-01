package com.szymon.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ExampleNotFoundException extends RuntimeException {

    public ExampleNotFoundException(String message) {
        super(message);
    }
}
