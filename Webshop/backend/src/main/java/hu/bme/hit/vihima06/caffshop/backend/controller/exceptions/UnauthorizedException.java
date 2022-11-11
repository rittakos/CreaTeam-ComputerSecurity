package hu.bme.hit.vihima06.caffshop.backend.controller.exceptions;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends AbstractException {

    public UnauthorizedException(String errorMessage) {
        super(errorMessage);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
