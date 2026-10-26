package io.shehzeb.taskmanager.api;

import io.shehzeb.taskmanager.security.JwtService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Minimal login endpoint for the demo frontend. It derives a stable user id from the username and
 * issues a signed JWT. In a real deployment this would delegate to an identity provider or the
 * companion <a href="https://github.com/sassad25/secure-auth-service">secure-auth-service</a>;
 * it is intentionally simple here so the full-stack flow is self-contained.
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public record LoginRequest(@NotBlank String username, @NotBlank String password) {
    }

    public record LoginResponse(String token, String tokenType, long expiresInSeconds, String username) {
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        // Deterministic user id from username so a user's tasks persist across logins in the demo.
        String userId = UUID.nameUUIDFromBytes(request.username().getBytes()).toString();
        String token = jwtService.createToken(userId, request.username());
        return new LoginResponse(token, "Bearer", jwtService.ttlSeconds(), request.username());
    }
}
