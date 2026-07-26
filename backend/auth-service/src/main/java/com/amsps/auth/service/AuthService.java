package com.amsps.auth.service;

import com.amsps.auth.dto.AuthResponse;
import com.amsps.auth.dto.LoginRequest;
import com.amsps.auth.dto.RegisterRequest;


public interface AuthService {


    AuthResponse register(RegisterRequest request);

  
    AuthResponse login(LoginRequest request);
}
