package hu.bme.hit.vihima06.caffshop.backend.integration;

import hu.bme.hit.vihima06.caffshop.backend.model.ERole;
import hu.bme.hit.vihima06.caffshop.backend.model.Role;
import hu.bme.hit.vihima06.caffshop.backend.model.User;
import hu.bme.hit.vihima06.caffshop.backend.repository.RoleRepository;
import hu.bme.hit.vihima06.caffshop.backend.repository.UserRepository;
import hu.bme.hit.vihima06.caffshop.backend.util.AuthHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class Admin {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthHelper authHelper;

    final String ADMIN_USERNAME = "admin";
    final String ADMIN_EMAIL = "admin";
    final String ADMIN_NAME = "admin";
    final String ADMIN_PASSWORD = "admin";

    final String USER_USERNAME = "user";
    final String USER_EMAIL = "user";
    final String USER_NAME = "user";
    final String USER_PASSWORD = "user";

    String adminAccessToken;
    String userAccessToken;

    @BeforeEach
    void setup() {
        Role adminRole = new Role(ERole.ROLE_ADMIN);
        Role userRole = new Role(ERole.ROLE_USER);
        roleRepository.saveAll(List.of(adminRole, userRole));

        User admin = new User(
                ADMIN_NAME,
                ADMIN_USERNAME,
                ADMIN_EMAIL,
                passwordEncoder.encode(ADMIN_PASSWORD),
                Set.of(adminRole, userRole)
        );
        userRepository.save(admin);

        User user = new User(
                USER_NAME,
                USER_USERNAME,
                USER_EMAIL,
                passwordEncoder.encode(USER_PASSWORD),
                Set.of(userRole)
        );
        userRepository.save(user);

        adminAccessToken = authHelper.getAccessToken(admin);
        userAccessToken = authHelper.getAccessToken(user);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void adminCanAccessUserList() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/admin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + adminAccessToken);

        mockMvc.perform(request).andExpect(status().isOk());
    }

    @Test
    void userCanNotAccessUserList() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/admin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + userAccessToken);

        mockMvc.perform(request).andExpect(status().isForbidden());
    }
}
