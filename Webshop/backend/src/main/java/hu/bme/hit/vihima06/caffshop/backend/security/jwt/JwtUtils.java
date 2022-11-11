package hu.bme.hit.vihima06.caffshop.backend.security.jwt;

import hu.bme.hit.vihima06.caffshop.backend.model.ERole;
import hu.bme.hit.vihima06.caffshop.backend.model.Role;
import hu.bme.hit.vihima06.caffshop.backend.model.User;
import hu.bme.hit.vihima06.caffshop.backend.security.service.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${auth.app.jwtSecret}")
    private String jwtSecret;

    @Value("${auth.app.jwtRefreshSecret}")
    private String jwtRefreshSecret;

    @Value("${auth.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${auth.app.jwtRefreshExpirationMs}")
    private int jwtRefreshExpirationMs;

    private static String NAME = "name";
    private static String EMAIL = "email";
    private static String ROLES = "roles";
    private static String ID = "id";

    public String generateJwtToken(UserDetailsImpl userDetails) {

        return Jwts.builder()
                .setSubject((userDetails.getUsername()))
                .setIssuedAt(new Date())
                .claim("type", "access")
                .claim(NAME, userDetails.getName())
                .claim(EMAIL, userDetails.getEmail())
                .claim(ID, userDetails.getId())
                .claim(ROLES, userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String generateJwtRefreshToken(UserDetailsImpl userDetails) {

        return Jwts.builder()
                .setSubject((userDetails.getUsername()))
                .setIssuedAt(new Date())
                .claim("type", "refresh")
                .setExpiration(new Date((new Date()).getTime() + jwtRefreshExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtRefreshSecret)
                .compact();
    }

    public User getUserFromJwtToken(String token) {
        String username = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
        String email = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().get(EMAIL, String.class);
        String name = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().get(NAME, String.class);
        Integer id = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().get(ID, Integer.class);
        List<String> roles = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().get(ROLES, List.class);

        User user = new User(name, username, email, null, roles.stream().map(r -> new Role(ERole.valueOf(r))).collect(Collectors.toSet()));
        user.setId(id);

        return user;
    }

    public String getUsernameFromJwtRefreshToken(String token) {
        return Jwts.parser().setSigningKey(jwtRefreshSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        return validateToken(authToken, jwtSecret);
    }

    public boolean validateJwtRefreshToken(String refreshToken) {
        return validateToken(refreshToken, jwtRefreshSecret);
    }

    public Date getRefreshTokenExpiration(String token) {
        return Jwts.parser().setSigningKey(jwtRefreshSecret).parseClaimsJws(token).getBody().getExpiration();
    }

    private boolean validateToken(String token, String secret) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}
