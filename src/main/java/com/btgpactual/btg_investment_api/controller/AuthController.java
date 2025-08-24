package com.btgpactual.btg_investment_api.controller;

import com.btgpactual.btg_investment_api.dto.*;
import com.btgpactual.btg_investment_api.model.User;
import com.btgpactual.btg_investment_api.security.JwtService;
import com.btgpactual.btg_investment_api.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@Validated
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = authService.authenticate(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestHeader("Authorization") String authHeader) {
        AuthResponse response = authService.refreshToken(authHeader);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody PasswordChangeRequest request) {
        authService.changePassword(userDetails.getUsername(), request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestParam String email) {
        authService.resetPassword(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password/confirm")
    public ResponseEntity<Void> confirmPasswordReset(
            @RequestParam String token,
            @RequestParam String newPassword) {
        authService.confirmPasswordReset(token, newPassword);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        UserResponse response = authService.getUserProfile(userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserResponse> updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody RegisterRequest request) {
        UserResponse response = authService.updateUserProfile(userDetails.getUsername(), request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = authService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/users/{email}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deactivateUser(@PathVariable String email) {
        authService.deactivateUser(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/users/{email}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> activateUser(@PathVariable String email) {
        authService.activateUser(email);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/validate-token")
    public ResponseEntity<Boolean> validateToken(@RequestParam String token) {
        boolean isValid = authService.validateToken(token);
        return ResponseEntity.ok(isValid);
    }
}
