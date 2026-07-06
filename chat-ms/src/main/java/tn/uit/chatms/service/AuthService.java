package tn.uit.chatms.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import tn.uit.chatms.dto.AuthLoginRequest;
import tn.uit.chatms.dto.AuthLoginResponse;
import tn.uit.chatms.dto.AuthRegisterRequest;
import tn.uit.chatms.entity.User;
import tn.uit.chatms.repository.UserRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret:uit-project-default-secret-key-min-256-bits}")
    private String jwtSecret;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthLoginResponse login(AuthLoginRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        String token = generateToken(user);

        return AuthLoginResponse.builder()
                .token(token)
                .userId(user.getUsername())
                .displayName(user.getDisplayName())
                .role(user.getRole())
                .build();
    }

    private String generateToken(User user) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        Instant now = Instant.now();

        return Jwts.builder()
                .subject(user.getUsername())
                .claim("displayName", user.getDisplayName())
                .claim("role", user.getRole())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(24, ChronoUnit.HOURS)))
                .signWith(key)
                .compact();
    }

    public AuthLoginResponse register(AuthRegisterRequest request) {
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        User user = User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .displayName(request.displayName() != null ? request.displayName() : request.username())
                .role("USER")
                .build();
        userRepository.save(user);
        String token = generateToken(user);
        return AuthLoginResponse.builder()
                .token(token)
                .userId(user.getUsername())
                .displayName(user.getDisplayName())
                .role(user.getRole())
                .build();
    }
}
