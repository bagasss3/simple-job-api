package com.example.jobApi.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.function.Function;

public interface JwtService {
    String extractUsername(String token);
    Claims extractAllClaims(String token);
    String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);
    Boolean isTokenValid(String token, UserDetails userDetails);
}
