package hu.bme.hit.vihima06.caffshop.backend.controller;

import hu.bme.hit.vihima06.caffshop.backend.api.AdminApi;
import hu.bme.hit.vihima06.caffshop.backend.models.ModifyUserRequest;
import hu.bme.hit.vihima06.caffshop.backend.models.UserDetailsResponse;
import hu.bme.hit.vihima06.caffshop.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AdminController implements AdminApi {

    @Autowired
    private UserService userService;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDetailsResponse>> getAdminUsers() {
        List<UserDetailsResponse> users = userService.getAllUsers();

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDetailsResponse> putAdminModifyUser(Integer id, ModifyUserRequest body) {
        UserDetailsResponse modifiedUser = userService.modifyUser(id, body);

        return new ResponseEntity<>(modifiedUser, HttpStatus.ACCEPTED);
    }

}
