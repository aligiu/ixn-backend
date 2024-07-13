package com.hng.ixn.auth;

import com.hng.ixn.auth.exception.EmailAlreadyExistsException;
import com.hng.ixn.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register-user")
    public ResponseEntity<AuthenticationResponse> registerUser(
            @RequestBody RegisterUserRequest request
    ) {
        try {
            AuthenticationResponse response = service.register(request);
            return ResponseEntity.ok(response);
        } catch (EmailAlreadyExistsException e) {
            AuthenticationResponse errorResponse = service.rejectAsEmailExists(request);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse); // Return conflict status with error message
        }
    }

    @PreAuthorize("hasRole('ADMIN')")  // only admins can create other admins
    @PostMapping("/register-admin-or-user")
    public ResponseEntity<AuthenticationResponse> registerAdminOrUser(
            @RequestBody RegisterAdminOrUserRequest request
    ) {
        try {
            AuthenticationResponse response = service.register(request);
            return ResponseEntity.ok(response);
        } catch (EmailAlreadyExistsException e) {
            AuthenticationResponse errorResponse = service.rejectAsEmailExists(request);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse); // Return conflict status with error message
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }
}