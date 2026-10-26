package io.shehzeb.taskmanager.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

/**
 * Issues HS256 JWTs for authenticated users. The algorithm is pinned to HS256 to match the
 * resource-server decoder (JJWT would otherwise select an algorithm from the key length).
 */
@Service
public class JwtService {

    private final JwtProperties properties;
    private final SecretKey key;

    public JwtService(JwtProperties properties) {
        this.properties = properties;
        this.key = Keys.hmacShaKeyFor(properties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(String subject, String username) {
        Instant now = Instant.now();
        return Jwts.builder()
                .issuer(properties.getIssuer())
                .subject(subject)
                .claim("username", username)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(properties.getTtl())))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public long ttlSeconds() {
        return properties.getTtl().toSeconds();
    }
}
