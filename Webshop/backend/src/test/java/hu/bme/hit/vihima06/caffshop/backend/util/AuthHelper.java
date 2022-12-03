package hu.bme.hit.vihima06.caffshop.backend.util;

import hu.bme.hit.vihima06.caffshop.backend.model.User;
import hu.bme.hit.vihima06.caffshop.backend.security.jwt.JwtUtils;
import hu.bme.hit.vihima06.caffshop.backend.security.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthHelper {

    @Autowired
    private JwtUtils jwtUtils;

    public String getAccessToken(User user) {
        return jwtUtils.generateJwtToken(UserDetailsImpl.build(user));
    }

}
