package com.find.doongji.member.payload.request;

import com.find.doongji.auth.enums.Role;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

    @Size(min = 1, max = 50, message = "Username must be between 1 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "Username can only contain letters, numbers, underscores, and hyphens")
    private String username;

    @Size(min = 1, max = 50, message = "Email must be between 1 and 50 characters")
    @Pattern(regexp = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$", message = "Email must be a valid email address")
    private String email;

    @Size(min = 6, max = 50, message = "Password must be between 8 and 50 characters")
    private String password;

    @Size(min = 6, max = 50, message = "Password must be between 8 and 50 characters")
    private String confirmPassword;

    private String name;

    private Role role;
}
