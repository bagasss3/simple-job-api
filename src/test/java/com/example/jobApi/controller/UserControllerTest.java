package com.example.jobApi.controller;

import com.example.jobApi.dto.ApiResponse;
import com.example.jobApi.exception.CustomException;
import com.example.jobApi.model.User;
import com.example.jobApi.service.UserService;
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
public class UserControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    public void testSaveUser_Successful() {
        User mockUser = new User();
        mockUser.setUsername("john_doe");
        mockUser.setPassword("password");

        when(userService.saveUser(mockUser)).thenReturn(mockUser);

        ResponseEntity<ApiResponse<?>> responseEntity = userController.saveUser(mockUser);

        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        ApiResponse<?> response = responseEntity.getBody();
        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals("Success create new user", response.getMessage());

        User responseData = (User) response.getData();
        Assertions.assertNotNull(responseData);
        Assertions.assertEquals("john_doe", responseData.getUsername());

        Mockito.verify(userService).saveUser(mockUser);
    }

    @Test
    public void testSaveUser_DuplicateUsername() {
        User mockUser = new User();
        mockUser.setUsername("john_doe");
        mockUser.setPassword("password");

        when(userService.saveUser(mockUser)).thenThrow(new CustomException("Username already exists", HttpStatus.BAD_REQUEST));

        ResponseEntity<ApiResponse<?>> responseEntity = userController.saveUser(mockUser);

        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        ApiResponse<?> response = responseEntity.getBody();
        Assertions.assertNotNull(response);
        Assertions.assertFalse(response.isSuccess());
        Assertions.assertEquals("Username already exists", response.getMessage());
        Assertions.assertNull(response.getData());

        Mockito.verify(userService).saveUser(mockUser);
    }

    @Test
    public void testSaveUser_InternalServerError() {
        User mockUser = new User();
        mockUser.setUsername("john_doe");
        mockUser.setPassword("password");

        when(userService.saveUser(mockUser)).thenThrow(new RuntimeException("Some internal error"));

        ResponseEntity<ApiResponse<?>> responseEntity = userController.saveUser(mockUser);

        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        ApiResponse<?> response = responseEntity.getBody();
        Assertions.assertNotNull(response);
        Assertions.assertFalse(response.isSuccess());
        Assertions.assertEquals("Error message: Some internal error", response.getMessage());
        Assertions.assertNull(response.getData());

        Mockito.verify(userService).saveUser(mockUser);
    }
}
