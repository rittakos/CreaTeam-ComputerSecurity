package hu.bme.hit.vihima06.caffshop.backend.controller;

import hu.bme.hit.vihima06.caffshop.backend.controller.exceptions.AbstractException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AbstractException.class)
    public ResponseEntity<String> handleExceptions(AbstractException e) {
        return new ResponseEntity(e.getMessage(), e.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnknownExceptions(Exception e) {
        return new ResponseEntity(e.getMessage(), HttpStatus.FORBIDDEN);
    }
}