package io.shehzeb.taskmanager.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/** Externalized JWT configuration. Supply the secret via env in production. */
@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {

    private String secret = "dev-only-secret-change-me-please-32bytes-minimum!!";
    private Duration ttl = Duration.ofHours(2);
    private String issuer = "task-manager";

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Duration getTtl() {
        return ttl;
    }

    public void setTtl(Duration ttl) {
        this.ttl = ttl;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }
}
