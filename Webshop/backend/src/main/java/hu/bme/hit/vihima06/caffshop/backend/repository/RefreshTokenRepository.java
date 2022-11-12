package hu.bme.hit.vihima06.caffshop.backend.repository;

import hu.bme.hit.vihima06.caffshop.backend.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findFirstByTokenHashAndUserId(String tokenHash, Integer userId);
    List<RefreshToken> findByExpirationLessThan(Date expiration);
    @Modifying
    @Query("delete from RefreshToken r where r.tokenHash = :tokenHash")
    void deleteAllByTokenHash(String tokenHash);
}