package com.hng.ixn.auth;

import com.hng.ixn.user.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterAdminOrUserRequest {
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private Role role;
}

