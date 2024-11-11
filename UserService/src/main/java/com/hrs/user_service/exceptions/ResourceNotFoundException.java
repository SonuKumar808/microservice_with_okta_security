package com.hrs.user_service.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException() {
        super("Resource not found on server! ");
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
