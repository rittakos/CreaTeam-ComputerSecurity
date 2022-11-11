package hu.bme.hit.vihima06.caffshop.backend.security.service;

import hu.bme.hit.vihima06.caffshop.backend.controller.exceptions.NotFoundException;
import hu.bme.hit.vihima06.caffshop.backend.model.User;
import hu.bme.hit.vihima06.caffshop.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty()) {
            throw new NotFoundException("User not found");
        }

        return UserDetailsImpl.build(user.get());
    }
}
