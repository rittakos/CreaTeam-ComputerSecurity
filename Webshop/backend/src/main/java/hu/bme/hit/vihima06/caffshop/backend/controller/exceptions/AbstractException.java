package hu.bme.hit.vihima06.caffshop.backend.controller.exceptions;

import org.springframework.http.HttpStatus;

public abstract class AbstractException extends RuntimeException {

    protected AbstractException(String errorMessage) {
        super(errorMessage);
    }

    public abstract HttpStatus getHttpStatus();
}
