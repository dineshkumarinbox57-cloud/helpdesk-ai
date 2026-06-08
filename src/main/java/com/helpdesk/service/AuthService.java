package com.helpdesk.service;

import com.helpdesk.config.JwtUtil;
import com.helpdesk.dto.LoginRequest;
import com.helpdesk.dto.LoginResponse;
import com.helpdesk.dto.RegisterRequest;
import com.helpdesk.entity.User;
import com.helpdesk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public String register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return "User already exists";
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        userRepository.save(user);

        return "Registration successful";
    }

    public LoginResponse login(LoginRequest request) {

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElse(null);

        if (user == null) {
        System.out.println("EMAIL = " + request.getEmail());
        System.out.println("USER FOUND = " + (user != null));  
        }

        boolean matches = passwordEncoder.matches(
        request.getPassword(),
        user.getPassword()
        );

        System.out.println("PASSWORD MATCHES = " + matches);

        if (matches) {
              String token =
                     JwtUtil.generateToken(user.getEmail());

            return new LoginResponse(
                    "Login successful",
                      token
);
        }

        return new LoginResponse("Invalid password");
    }
}