package com.example.jobApi.controller;

import com.example.jobApi.dto.ApiResponse;
import com.example.jobApi.dto.AuthenticationResponse;
import com.example.jobApi.exception.CustomException;
import com.example.jobApi.model.User;
import com.example.jobApi.service.AuthService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {
    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    public void testLogin_Successful() {
        // Mock data
        User mockUser = new User();
        mockUser.setUsername("john_doe");
        mockUser.setPassword("password");

        AuthenticationResponse mockToken = new AuthenticationResponse();
        mockToken.setToken("your_mocked_jwt_token_here");

        // Mocking behavior of AuthService
        when(authService.login(mockUser)).thenReturn(mockToken);

        // Call the method to test
        ResponseEntity<ApiResponse<?>> responseEntity = authController.login(mockUser);

        // Verify the results
        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        ApiResponse<?> response = responseEntity.getBody();
        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals("Success login", response.getMessage());

        AuthenticationResponse responseToken = (AuthenticationResponse) response.getData();
        Assertions.assertNotNull(responseToken);
        Assertions.assertEquals("your_mocked_jwt_token_here", responseToken.getToken());

        // Verify that the AuthService.login() method was called with the correct argument
        Mockito.verify(authService).login(mockUser);
    }

    @Test
    public void testLogin_InvalidCredentials() {
        User mockUser = new User();
        mockUser.setUsername("john_doe");
        mockUser.setPassword("wrong_password");

        when(authService.login(mockUser)).thenThrow(new CustomException("Wrong password", HttpStatus.BAD_REQUEST));

        ResponseEntity<ApiResponse<?>> responseEntity = authController.login(mockUser);

        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        ApiResponse<?> response = responseEntity.getBody();
        Assertions.assertNotNull(response);
        Assertions.assertFalse(response.isSuccess());
        Assertions.assertEquals("Wrong password", response.getMessage());
        Assertions.assertNull(response.getData());

        Mockito.verify(authService).login(mockUser);
    }

    @Test
    public void testLogin_InternalServerError() {
        User mockUser = new User();
        mockUser.setUsername("john_doe");
        mockUser.setPassword("password");

        when(authService.login(mockUser)).thenThrow(new RuntimeException("Some internal error"));

        ResponseEntity<ApiResponse<?>> responseEntity = authController.login(mockUser);

        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        ApiResponse<?> response = responseEntity.getBody();
        Assertions.assertNotNull(response);
        Assertions.assertFalse(response.isSuccess());
        Assertions.assertEquals("Error message: Some internal error", response.getMessage());
        Assertions.assertNull(response.getData());

        // Verify that the AuthService.login() method was called with the correct argument
        Mockito.verify(authService).login(mockUser);
    }


}
