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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final LoggedInUserService loggedInUserService;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, LoggedInUserService loggedInUserService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.loggedInUserService = loggedInUserService;
        logger.info("Initialized");
    }

    public List<UserDetailsResponse> getAllUsers() {
        User loggedInUser = loggedInUserService.getLoggedInUser();
        logger.info("Listing all users for admin user {}", loggedInUser.getUsername());
        return userRepository.findAll().stream().map(u -> UserMapper.INSTANCE.userToUserDetailsResponse(u)).toList();
    }

    public UserDetailsResponse modifyUser(Integer id, ModifyUserRequest modifyUserRequest) {
        User loggedInUser = loggedInUserService.getLoggedInUser();

        logger.info("Admin {} modifies user with id {}", loggedInUser.getUsername(), id);

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

        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));

        user.setName(modifyUserRequest.getName());
        user.setEmail(modifyUserRequest.getEmail());

        Set<Role> roles = roleRepository.findAllByNameIn(parsedRoles);

        user.setRoles(roles);

        userRepository.save(user);

        logger.info("Admin {} successfully modified ", loggedInUser.getUsername(), user.getUsername());

        return UserMapper.INSTANCE.userToUserDetailsResponse(user);
    }
}
