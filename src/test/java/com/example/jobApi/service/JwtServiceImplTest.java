package com.example.jobApi.service;

import com.example.jobApi.config.ApplicationConfig;
import com.example.jobApi.model.User;
import com.example.jobApi.service.impl.JwtServiceImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtServiceImplTest {
    @Mock
    private ApplicationConfig applicationConfig;

    @InjectMocks
    private JwtServiceImpl jwtService;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(applicationConfig, "SECRET_KEY", "06cf09692868af8043948eaf1558fe2340980360a88bbe3e20ea855542302a4d");
    }

    @Test
    public void testGenerateToken_Successful() {
        String mockUsername = "john_doe";
        User userDetails = new User();
        userDetails.setUsername(mockUsername);
        userDetails.setPassword("password");
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("customKey", "customValue");

        String jwtToken = jwtService.generateToken(extraClaims, userDetails);

        Assertions.assertNotNull(jwtToken);
        Assertions.assertTrue(jwtToken.length() > 0);
    }

    @Test
    public void testIsTokenValid_ValidToken() {
        String mockUsername = "john_doe";
        User userDetails = new User();
        userDetails.setUsername(mockUsername);
        userDetails.setPassword("password");
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("customKey", "customValue");

        String jwtToken = Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(mockUsername)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(applicationConfig.SECRET_KEY)))
                .compact();

        Boolean isValidToken = jwtService.isTokenValid(jwtToken, userDetails);

        Assertions.assertTrue(isValidToken);
    }
}
