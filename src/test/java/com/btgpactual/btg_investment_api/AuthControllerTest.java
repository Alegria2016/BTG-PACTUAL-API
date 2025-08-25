package com.btgpactual.btg_investment_api;

import com.btgpactual.btg_investment_api.controller.AuthController;
import com.btgpactual.btg_investment_api.dto.AuthRequest;
import com.btgpactual.btg_investment_api.dto.AuthResponse;
import com.btgpactual.btg_investment_api.dto.RegisterRequest;
import com.btgpactual.btg_investment_api.dto.UserResponse;
import com.btgpactual.btg_investment_api.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void register_ShouldReturnAuthResponse_WhenValidRequest() throws Exception {
        // Arrange
        RegisterRequest request = new RegisterRequest("test@example.com", "password123", "John", "Doe", List.of("USER"));
        AuthResponse response = new AuthResponse("jwt-token", "test@example.com", "John", "Doe", List.of("USER"));

        when(authService.register(any(RegisterRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));

        verify(authService, times(1)).register(any(RegisterRequest.class));
    }

    @Test
    void register_ShouldReturnBadRequest_WhenServiceThrowsRuntimeException() throws Exception {
        // Arrange
        RegisterRequest request = new RegisterRequest("test@example.com", "password123", "John", "Doe", List.of("USER"));
        String errorMessage = "Email already exists";

        when(authService.register(any(RegisterRequest.class))).thenThrow(new RuntimeException(errorMessage));

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(errorMessage));

        verify(authService, times(1)).register(any(RegisterRequest.class));
    }


    @Test
    void login_ShouldReturnAuthResponse_WhenValidCredentials() throws Exception {
        // Arrange
        AuthRequest request = new AuthRequest("test@example.com", "password123");
        AuthResponse response = new AuthResponse("jwt-token", "test@example.com", "John", "Doe", List.of("USER"));

        when(authService.authenticate(any(AuthRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));

        verify(authService, times(1)).authenticate(any(AuthRequest.class));
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers_WhenAdminRole() throws Exception {
        // Arrange
        UserResponse userResponse = new UserResponse("1", "test@example.com", "John", "Doe", List.of("USER"), true);
        List<UserResponse> users = List.of(userResponse);

        when(authService.getAllUsers()).thenReturn(users);

        // Act & Assert
        mockMvc.perform(get("/api/v1/auth/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].email").value("test@example.com"))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[0].roles[0]").value("USER"))
                .andExpect(jsonPath("$[0].enabled").value(true));

        verify(authService, times(1)).getAllUsers();
    }

    @Test
    void getAllUsers_ShouldReturnEmptyList_WhenNoUsersExist() throws Exception {
        // Arrange
        when(authService.getAllUsers()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/v1/auth/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(authService, times(1)).getAllUsers();
    }

    @Test
    void register_ShouldReturnBadRequest_WhenInvalidRequest() throws Exception {
        // Arrange - Request sin email (inválido)
        RegisterRequest invalidRequest = new RegisterRequest("", "password123", "John", "Doe", List.of("USER"));

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(authService, never()).register(any(RegisterRequest.class));
    }

    @Test
    void login_ShouldReturnBadRequest_WhenInvalidRequest() throws Exception {
        // Arrange - Request sin password (inválido)
        AuthRequest invalidRequest = new AuthRequest("test@example.com", "");

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(authService, never()).authenticate(any(AuthRequest.class));
    }
}