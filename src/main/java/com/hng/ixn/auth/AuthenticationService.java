package com.hng.ixn.auth;

import com.hng.ixn.auth.exception.EmailAlreadyExistsException;
import com.hng.ixn.config.JwtService;
import com.hng.ixn.user.Role;
import com.hng.ixn.user.User;
import com.hng.ixn.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterUserRequest request) {
        RegisterAdminOrUserRequest requestAdminOrUser = new RegisterAdminOrUserRequest();
        requestAdminOrUser.setEmail(request.getEmail());
        requestAdminOrUser.setLastname(request.getLastname());
        requestAdminOrUser.setFirstname(request.getFirstname());
        requestAdminOrUser.setPassword(request.getPassword());
        requestAdminOrUser.setRole(Role.ROLE_USER);
        return register(requestAdminOrUser);
    }

    public AuthenticationResponse register(RegisterAdminOrUserRequest request) {
        if (repository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        var user = User.builder().email(request.getEmail()).password(
                passwordEncoder.encode(request.getPassword())).role(request.getRole()).build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).email(request.getEmail()).isAdmin(
                request.getRole() == Role.ROLE_ADMIN).build();
    }

    public AuthenticationResponse rejectAsEmailExists(RegisterAdminOrUserRequest request) {
        return AuthenticationResponse.builder().errorMessage("Email already exists").email(
                request.getEmail()).build();
    }

    public AuthenticationResponse rejectAsEmailExists(RegisterUserRequest request) {
        return AuthenticationResponse.builder().errorMessage("Email already exists").email(
                request.getEmail()).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        var user = repository.findByEmail(request.getEmail()).orElseThrow();
        logger.info("*** Authentication successful for user: {}", request.getEmail());

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).email(request.getEmail()).isAdmin(
                user.getRole() == Role.ROLE_ADMIN).build();
    }
}
