package hu.bme.hit.vihima06.caffshop.backend.controller.exceptions;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends AbstractException {

    public ForbiddenException(String errorMessage) {
        super(errorMessage);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.FORBIDDEN;
    }
}
