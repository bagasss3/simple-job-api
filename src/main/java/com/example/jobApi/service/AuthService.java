package com.example.jobApi.service;

import com.example.jobApi.dto.AuthenticationResponse;
import com.example.jobApi.model.User;

public interface AuthService {
    AuthenticationResponse login(User user);
}
