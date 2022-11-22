package hu.bme.hit.vihima06.caffshop.backend.service;

import hu.bme.hit.vihima06.caffshop.backend.controller.exceptions.BadRequestException;
import hu.bme.hit.vihima06.caffshop.backend.controller.exceptions.NotFoundException;
import hu.bme.hit.vihima06.caffshop.backend.mapper.UserMapper;
import hu.bme.hit.vihima06.caffshop.backend.model.ERole;
import hu.bme.hit.vihima06.caffshop.backend.model.Role;
import hu.bme.hit.vihima06.caffshop.backend.model.User;
import hu.bme.hit.vihima06.caffshop.backend.models.ModifyUserRequest;
import hu.bme.hit.vihima06.caffshop.backend.models.UserDetailsResponse;
import hu.bme.hit.vihima06.caffshop.backend.repository.RoleRepository;
import hu.bme.hit.vihima06.caffshop.backend.repository.UserRepository;
import hu.bme.hit.vihima06.caffshop.backend.security.service.LoggedInUserService;
import hu.bme.hit.vihima06.caffshop.backend.service.util.EmailValidator;
import hu.bme.hit.vihima06.caffshop.backend.service.util.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private LoggedInUserService loggedInUserService;

    public List<UserDetailsResponse> getAllUsers() {
        return userRepository.findAll().stream().map(u -> UserMapper.INSTANCE.userToUserDetailsResponse(u)).toList();
    }


    public UserDetailsResponse modifyUser(Integer id, ModifyUserRequest modifyUserRequest) {
        User loggedInUser = loggedInUserService.getLoggedInUser();

        List<ERole> parsedRoles = modifyUserRequest.getRoles().stream().map(r -> ERole.valueOf(r)).toList();

        if (loggedInUser.getId().equals(id) && parsedRoles.indexOf(ERole.ROLE_ADMIN) == -1) {
            throw new BadRequestException("Admins can not remove their own admin role");
        }

        if (!EmailValidator.validateEmail(modifyUserRequest.getEmail())) {
            throw new BadRequestException("Invalid email");
        }

        if (!UserValidator.validateName(modifyUserRequest.getName())) {
            throw new BadRequestException("Name should be at least 4 characters long!");
        }

        if (userRepository.existsByEmailAndIdNot(modifyUserRequest.getEmail(), id)) {
            throw new BadRequestException("Email already in use");
        }

        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            throw new NotFoundException("User not found");
        }

        User storedUser = user.get();

        storedUser.setName(modifyUserRequest.getEmail());
        storedUser.setEmail(modifyUserRequest.getEmail());

        Set<Role> roles = roleRepository.findAllByNameIn(parsedRoles);

        storedUser.setRoles(roles);

        userRepository.save(storedUser);

        return UserMapper.INSTANCE.userToUserDetailsResponse(storedUser);
    }
}
