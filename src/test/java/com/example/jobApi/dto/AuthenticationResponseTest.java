package com.example.jobApi.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AuthenticationResponseTest {
    @Test
    public void AuthenticationResponseTest(){
        String token = "tokenTest";
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();

        authenticationResponse.setToken(token);

        Assertions.assertEquals(token, authenticationResponse.getToken());
    }

}
