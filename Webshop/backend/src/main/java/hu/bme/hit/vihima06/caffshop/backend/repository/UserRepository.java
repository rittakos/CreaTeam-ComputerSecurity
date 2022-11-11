package hu.bme.hit.vihima06.caffshop.backend.repository;

import hu.bme.hit.vihima06.caffshop.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
