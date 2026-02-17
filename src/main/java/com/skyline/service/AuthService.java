package com.skyline.service;

import com.skyline.dto.LoginRequest;
import com.skyline.dto.LoginResponse;

public interface AuthService {
    LoginResponse loginUser(LoginRequest request);
}
