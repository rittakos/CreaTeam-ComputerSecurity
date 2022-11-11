package hu.bme.hit.vihima06.caffshop.backend.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "refresh_token")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false)
    private Integer id;

    @Column(name = "token_hash", nullable = false)
    private String tokenHash;

    @Column(name = "expiration", nullable = false)
    private Date expiration;

    @ManyToOne
    private User user;

    public RefreshToken() {
    }

    public RefreshToken(String tokenHash, Date expiration, User user) {
        this.tokenHash = tokenHash;
        this.expiration = expiration;
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTokenHash() {
        return tokenHash;
    }

    public void setTokenHash(String tokenHash) {
        this.tokenHash = tokenHash;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }
}
