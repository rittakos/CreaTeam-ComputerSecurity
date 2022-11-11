package hu.bme.hit.vihima06.caffshop.backend.controller;

import hu.bme.hit.vihima06.caffshop.backend.api.AuthApi;
import hu.bme.hit.vihima06.caffshop.backend.models.*;
import hu.bme.hit.vihima06.caffshop.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController implements AuthApi {
    @Autowired
    private AuthService authService;

    @Override
    public ResponseEntity<LoginResponse> postAuthLogin(LoginRequest body) {
        LoginResponse loginResponse = authService.login(body);

        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<NewTokenResponse> postAuthLoginRefresh(RefreshTokenRequest body) {
        NewTokenResponse newTokenResponse = authService.refreshLogin(body.getRefreshToken());

        return new ResponseEntity<>(newTokenResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> postAuthLogout(RefreshTokenRequest body) {
        authService.logout(body.getRefreshToken());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Void> postAuthRegister(RegistrationRequest body) {
        authService.register(body);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
