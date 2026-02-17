package com.skyline.service.impl;

import com.skyline.dto.LoginRequest;
import com.skyline.dto.LoginResponse;
import com.skyline.entity.Users;
import com.skyline.exception.NotFoundException;
import com.skyline.repository.UsersRepository;
import com.skyline.service.AuthService;
import com.skyline.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    @Autowired
    private final UsersRepository usersRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;


    @Override
    public LoginResponse loginUser(LoginRequest request) {
        Users users = usersRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new NotFoundException("Invalid username"));

        if (!passwordEncoder.matches(request.getPassword(), users.getPassword())) {
            throw new NotFoundException("Invalid password");
        }

        String token = jwtUtil.generateToken(users.getUsername(), users.getRoles());

        return LoginResponse.builder()
                .message("Login successful")
                .username(users.getUsername())
                .userId(users.getId())
                .token(token)
                .build();
    }
}
