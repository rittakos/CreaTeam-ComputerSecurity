package hu.bme.hit.vihima06.caffshop.backend.security.service;

import hu.bme.hit.vihima06.caffshop.backend.model.ERole;
import hu.bme.hit.vihima06.caffshop.backend.model.Role;
import hu.bme.hit.vihima06.caffshop.backend.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static hu.bme.hit.vihima06.caffshop.backend.model.ERole.ROLE_ADMIN;

@Service
public class LoggedInUserService {

    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Set<Role> roles = userDetails.getAuthorities()
                .stream()
                .map(a -> new Role(ERole.valueOf(a.getAuthority())))
                .collect(Collectors.toSet());

        User user = new User(userDetails.getName(), userDetails.getUsername(), userDetails.getEmail(), null, roles);
        user.setId(userDetails.getId());

        return user;
    }

    public boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return authorities.stream().map(GrantedAuthority::getAuthority).toList().contains(ROLE_ADMIN.name());
    }
}