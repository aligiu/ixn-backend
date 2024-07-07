package com.hng.ixn.auth;

import com.hng.ixn.auth.exception.EmailAlreadyExistsException;
import com.hng.ixn.config.JwtService;
import com.hng.ixn.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.hng.ixn.user.User;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    public AuthenticationResponse register(RegisterRequest request) {
        if (repository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse rejectAsEmailExists(RegisterRequest request) {
        return AuthenticationResponse.builder()
                .errorMessage("Email already exists")
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        logger.info("*** Authentication successful for user: {}", request.getEmail());
        System.out.println(repository.findByEmail(request.getEmail()));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = repository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
