package com.socio.controller;

import com.socio.dto.LoginRequest;
import com.socio.dto.LoginResponse;
import com.socio.dto.UserRegistrationRequest;
import com.socio.model.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "2. User Management", description = "APIs for user registration, login, and profile management")
public class UserController {

    // In-memory user store to simulate a database
    private static final Map<String, User> userStore = new ConcurrentHashMap<>();
    private static final AtomicLong userIdCounter = new AtomicLong();
    private final PasswordEncoder passwordEncoder;

    public UserController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        // Add a sample user
        String encodedPassword = passwordEncoder.encode("password123");
        User sampleUser = new User(userIdCounter.incrementAndGet(), "user@example.com", encodedPassword);
        userStore.put(sampleUser.getEmail(), sampleUser);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        if (userStore.containsKey(request.email())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email is already registered.");
        }
        String hashedPassword = passwordEncoder.encode(request.password());
        User newUser = new User(userIdCounter.incrementAndGet(), request.email(), hashedPassword);
        userStore.put(newUser.getEmail(), newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        User user = userStore.get(request.email());
        if (user == null || !passwordEncoder.matches(request.password(), user.getHashedPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials.");
        }
        // In a real app, generate a JWT token here
        String dummyJwt = "dummy-jwt-for-" + user.getEmail();
        return ResponseEntity.ok(new LoginResponse(dummyJwt, user.getRole()));
    }
    
    @GetMapping("/{email}")
    public ResponseEntity<User> getUser(@PathVariable String email) {
        User user = userStore.get(email);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }
}