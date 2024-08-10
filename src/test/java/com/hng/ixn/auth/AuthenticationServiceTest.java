package com.hng.ixn.auth;

import com.hng.ixn.auth.exception.EmailAlreadyExistsException;
import com.hng.ixn.config.JwtService;
import com.hng.ixn.user.Role;
import com.hng.ixn.user.User;
import com.hng.ixn.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerShouldCreateUserSuccessfully() {
        RegisterAdminOrUserRequest request = new RegisterAdminOrUserRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");
        request.setRole(Role.ROLE_USER);

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");

        AuthenticationResponse response = authenticationService.register(request);

        verify(userRepository).save(any(User.class));
        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
        assertEquals("test@example.com", response.getEmail());
        assertFalse(response.getIsAdmin());
    }

    @Test
    void registerShouldThrowExceptionWhenEmailExists() {
        RegisterAdminOrUserRequest request = new RegisterAdminOrUserRequest();
        request.setEmail("test@example.com");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> authenticationService.register(request));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void rejectAsEmailExistsForAdminOrUser() {
        RegisterAdminOrUserRequest request = new RegisterAdminOrUserRequest();
        request.setEmail("test@example.com");

        AuthenticationResponse response = authenticationService.rejectAsEmailExists(request);

        assertNotNull(response);
        assertEquals("test@example.com", response.getEmail());
        assertEquals("Email already exists", response.getErrorMessage());
    }

    @Test
    void rejectAsEmailExistsForUser() {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setEmail("test@example.com");

        AuthenticationResponse response = authenticationService.rejectAsEmailExists(request);

        assertNotNull(response);
        assertEquals("test@example.com", response.getEmail());
        assertEquals("Email already exists", response.getErrorMessage());
    }

    @Test
    void authenticateShouldReturnValidResponse() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");

        User user = User.builder()
                .email(request.getEmail())
                .password("encodedPassword")
                .role(Role.ROLE_USER)
                .build();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");

        AuthenticationResponse response = authenticationService.authenticate(request);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
        assertEquals("test@example.com", response.getEmail());
        assertFalse(response.getIsAdmin());
    }

    @Test
    void authenticateShouldThrowExceptionWhenUserNotFound() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> authenticationService.authenticate(request));
        verify(authenticationManager, never()).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}
