package com.btgpactual.btg_investment_api.service;

import com.btgpactual.btg_investment_api.dto.*;
import com.btgpactual.btg_investment_api.security.JwtService;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import com.btgpactual.btg_investment_api.model.User;
import com.btgpactual.btg_investment_api.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional

public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(RegisterRequest request) {
        LOGGER.info("Intentando registrar usuario: {}", request.email());

        if (userRepository.findByEmail(request.email()).isPresent()) {
            LOGGER.warn("Intento de registro con email ya existente: {}", request.email());
            throw new RuntimeException("El usuario ya existe");
        }

        var user = new User(
                request.email(),
                passwordEncoder.encode(request.password()),
                request.firstName(),
                request.lastName(),
                request.roles() != null ? request.roles() : List.of("ROLE_CLIENT")
        );

        userRepository.save(user);
        LOGGER.info("Usuario registrado exitosamente: {}", request.email());

        var jwtToken = jwtService.generateToken(user);
        return new AuthResponse(
                jwtToken,
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRoles()
        );
    }

    public AuthResponse authenticate(AuthRequest request) {
        LOGGER.info("Autenticando usuario: {}", request.email());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );

            var user = userRepository.findByEmail(request.email())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            if (!user.isEnabled()) {
                throw new RuntimeException("Usuario deshabilitado");
            }

            var jwtToken = jwtService.generateToken(user);
            LOGGER.info("Usuario autenticado exitosamente: {}", request.email());

            return new AuthResponse(
                    jwtToken,
                    user.getEmail(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getRoles()
            );

        } catch (BadCredentialsException e) {
            LOGGER.warn("Credenciales inválidas para usuario: {}", request.email());
            throw new RuntimeException("Credenciales inválidas");
        }
    }

    public AuthResponse refreshToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Token de refresh inválido");
        }

        String refreshToken = authHeader.substring(7);

        if (!jwtService.validateToken(refreshToken)) {
            throw new RuntimeException("Token de refresh inválido o expirado");
        }

        String username = jwtService.extractUsername(refreshToken);
        var user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!user.isEnabled()) {
            throw new RuntimeException("Usuario deshabilitado");
        }

        var newToken = jwtService.generateToken(user);
        LOGGER.info("Token refrescado para usuario: {}", username);

        return new AuthResponse(
                newToken,
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRoles()
        );
    }

    public void changePassword(String email, PasswordChangeRequest request) {
        LOGGER.info("Cambiando password para usuario: {}", email);

        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            LOGGER.warn("Password actual incorrecto para usuario: {}", email);
            throw new RuntimeException("Password actual incorrecto");
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
        LOGGER.info("Password cambiado exitosamente para usuario: {}", email);
    }

    public void resetPassword(String email) {
        LOGGER.info("Solicitando reset de password para usuario: {}", email);

        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Generar token temporal para reset (podría enviarse por email)
        String resetToken = jwtService.generateToken(
                Map.of("reset", true),
                user
        );

        // En producción, aquí se enviaría el token por email
        LOGGER.info("Token de reset generado para {}: {}", email, resetToken);

        // Simular envío de email
        simulatePasswordResetEmail(email, resetToken);
    }

    public void confirmPasswordReset(String token, String newPassword) {
        if (!jwtService.validateToken(token)) {
            throw new RuntimeException("Token de reset inválido");
        }

        Claims claims = jwtService.extractAllClaims(token);
        if (!Boolean.TRUE.equals(claims.get("reset", Boolean.class))) {
            throw new RuntimeException("Token no es de reset de password");
        }

        String email = jwtService.extractUsername(token);
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        LOGGER.info("Password reset confirmado para usuario: {}", email);
    }

    public UserResponse getUserProfile(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRoles(),
                user.isEnabled()
        );
    }

    public UserResponse updateUserProfile(String email, RegisterRequest request) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());

        if (request.roles() != null && !request.roles().isEmpty()) {
            user.setRoles(request.roles());
        }

        userRepository.save(user);
        LOGGER.info("Perfil actualizado para usuario: {}", email);

        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRoles(),
                user.isEnabled()

        );
    }

    public void deactivateUser(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.setEnabled(false);
        userRepository.save(user);
        LOGGER.info("Usuario desactivado: {}", email);
    }

    public void activateUser(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.setEnabled(true);
        userRepository.save(user);
        LOGGER.info("Usuario activado: {}", email);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getEmail(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getRoles(),
                        user.isEnabled()
                ))
                .collect(Collectors.toList());
    }

    public boolean validateToken(String token) {
        return jwtService.validateToken(token);
    }

    public UserDetails getUserDetailsFromToken(String token) {
        String username = jwtService.extractUsername(token);
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    private void simulatePasswordResetEmail(String email, String resetToken) {
        LOGGER.info("=== SIMULACIÓN DE EMAIL ===");
        LOGGER.info("Para: {}", email);
        LOGGER.info("Asunto: Solicitud de reset de password");
        LOGGER.info("Mensaje: Use el siguiente token para resetear su password: {}", resetToken);
        LOGGER.info("Enlace: http://localhost:8080/api/auth/reset-password/confirm?token={}", resetToken);
        LOGGER.info("===========================");
    }
}
