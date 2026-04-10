package com.swiftpay.security;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.swiftpay.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistrationForm {
	
	@NotBlank(message="Username is required")
    @Size(min=5, message="Username must be at least 5 characters")
    private String username;
	
	@NotBlank(message="Password is required")
    @Size(min=6, message="Password must be at least 6 characters")
    private String password;
    private String fullname;
    private String phoneNumber;

    public User toUser(PasswordEncoder passwordEncoder) {
        return new User(
            username, 
            passwordEncoder.encode(password), // SCRAMBLE the password!
            fullname, 
            phoneNumber
        );
    }
}