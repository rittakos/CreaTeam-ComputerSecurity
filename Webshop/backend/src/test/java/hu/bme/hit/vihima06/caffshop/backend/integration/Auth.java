package hu.bme.hit.vihima06.caffshop.backend.integration;

import hu.bme.hit.vihima06.caffshop.backend.model.ERole;
import hu.bme.hit.vihima06.caffshop.backend.model.Role;
import hu.bme.hit.vihima06.caffshop.backend.model.User;
import hu.bme.hit.vihima06.caffshop.backend.models.LoginRequest;
import hu.bme.hit.vihima06.caffshop.backend.models.RefreshTokenRequest;
import hu.bme.hit.vihima06.caffshop.backend.repository.RefreshTokenRepository;
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

import static hu.bme.hit.vihima06.caffshop.backend.util.TestHelper.asJsonString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class Auth {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthHelper authHelper;

    final String ADMIN_USERNAME = "admin";
    final String ADMIN_EMAIL = "admin";
    final String ADMIN_NAME = "admin";
    final String ADMIN_PASSWORD ="admin";

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
        refreshTokenRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void logout() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.post("/auth/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + adminAccessToken)
                .content(asJsonString(new RefreshTokenRequest().refreshToken("")));

        mockMvc.perform(request).andExpect(status().isNoContent());
    }

    @Test
    void login() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(new LoginRequest().username(USER_USERNAME).password(USER_PASSWORD)));

        mockMvc.perform(request).andExpect(status().isOk());
    }
}
