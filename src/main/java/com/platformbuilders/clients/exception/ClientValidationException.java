package com.platformbuilders.clients.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ClientValidationException extends RuntimeException {

    public ClientValidationException(String message) {
        super(message);
    }
}
