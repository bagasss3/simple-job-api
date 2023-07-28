package com.example.jobApi.service;

import com.example.jobApi.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {
    User saveUser(User user);
}
