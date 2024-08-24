package com.hng.ixn.auth;

import com.hng.ixn.auth.exception.EmailAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthenticationControllerTest {

    @Mock
    private AuthenticationService service;

    @InjectMocks
    private AuthenticationController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_Success() {
        RegisterUserRequest request = new RegisterUserRequest();
        AuthenticationResponse response = AuthenticationResponse.builder()
                .token("sample-token")
                .email("user@example.com")
                .isAdmin(false)
                .build();

        when(service.register(any(RegisterUserRequest.class))).thenReturn(response);

        ResponseEntity<AuthenticationResponse> result = controller.registerUser(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("sample-token", result.getBody().getToken());
        assertEquals("user@example.com", result.getBody().getEmail());
        assertFalse(result.getBody().getIsAdmin());
        assertNull(result.getBody().getErrorMessage());
    }

    @Test
    void registerUser_EmailAlreadyExists() {
        RegisterUserRequest request = new RegisterUserRequest();
        AuthenticationResponse errorResponse = AuthenticationResponse.builder()
                .errorMessage("Email already exists")
                .build();

        when(service.register(any(RegisterUserRequest.class))).thenThrow(new EmailAlreadyExistsException("Email already exists"));
        when(service.rejectAsEmailExists(any(RegisterUserRequest.class))).thenReturn(errorResponse);

        ResponseEntity<AuthenticationResponse> result = controller.registerUser(request);

        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        assertNull(result.getBody().getToken());
        assertNull(result.getBody().getEmail());
        assertNull(result.getBody().getIsAdmin());
        assertEquals("Email already exists", result.getBody().getErrorMessage());
    }

    @Test
    void registerAdminOrUser_Success() {
        RegisterAdminOrUserRequest request = new RegisterAdminOrUserRequest();
        AuthenticationResponse response = AuthenticationResponse.builder()
                .token("sample-admin-token")
                .email("admin@example.com")
                .isAdmin(true)
                .build();

        when(service.register(any(RegisterAdminOrUserRequest.class))).thenReturn(response);

        ResponseEntity<AuthenticationResponse> result = controller.registerAdminOrUser(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("sample-admin-token", result.getBody().getToken());
        assertEquals("admin@example.com", result.getBody().getEmail());
        assertTrue(result.getBody().getIsAdmin());
        assertNull(result.getBody().getErrorMessage());
    }

    @Test
    void registerAdminOrUser_EmailAlreadyExists() {
        RegisterAdminOrUserRequest request = new RegisterAdminOrUserRequest();
        AuthenticationResponse errorResponse = AuthenticationResponse.builder()
                .errorMessage("Email already exists")
                .build();

        when(service.register(any(RegisterAdminOrUserRequest.class))).thenThrow(new EmailAlreadyExistsException("Email already exists"));
        when(service.rejectAsEmailExists(any(RegisterAdminOrUserRequest.class))).thenReturn(errorResponse);

        ResponseEntity<AuthenticationResponse> result = controller.registerAdminOrUser(request);

        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        assertNull(result.getBody().getToken());
        assertNull(result.getBody().getEmail());
        assertNull(result.getBody().getIsAdmin());
        assertEquals("Email already exists", result.getBody().getErrorMessage());
    }

    @Test
    void authenticate_Success() {
        AuthenticationRequest request = new AuthenticationRequest();
        AuthenticationResponse response = AuthenticationResponse.builder()
                .token("auth-token")
                .email("user@example.com")
                .isAdmin(false)
                .build();

        when(service.authenticate(any(AuthenticationRequest.class))).thenReturn(response);

        ResponseEntity<AuthenticationResponse> result = controller.authenticate(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("auth-token", result.getBody().getToken());
        assertEquals("user@example.com", result.getBody().getEmail());
        assertFalse(result.getBody().getIsAdmin());
        assertNull(result.getBody().getErrorMessage());
        verify(service, times(1)).authenticate(request);
    }
}
