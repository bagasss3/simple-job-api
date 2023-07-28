package com.example.jobApi.service;

import com.example.jobApi.exception.CustomException;
import com.example.jobApi.model.User;
import com.example.jobApi.repository.UserRepository;
import com.example.jobApi.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testSaveUser_Successful() {
        User mockUser = new User();
        mockUser.setUsername("john_doe");
        mockUser.setPassword("password123");

        when(userRepository.findByUsername("john_doe")).thenReturn(Optional.empty());
        when(userRepository.save(mockUser)).thenReturn(mockUser);

        User savedUser = userService.saveUser(mockUser);

        Assertions.assertNotNull(savedUser);
        Assertions.assertEquals(mockUser.getUsername(), savedUser.getUsername());
        Assertions.assertNotEquals("password123", savedUser.getPassword());

        Mockito.verify(userRepository).findByUsername("john_doe");
        Mockito.verify(userRepository).save(mockUser);
    }

    @Test
    public void testSaveUser_UsernameExists() {
        User mockUser = new User();
        mockUser.setUsername("john_doe");
        mockUser.setPassword("password123");

        when(userRepository.findByUsername("john_doe")).thenReturn(Optional.of(mockUser));

        CustomException exception = Assertions.assertThrows(CustomException.class, () -> userService.saveUser(mockUser));
        Assertions.assertEquals("Username already exists", exception.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());

        Mockito.verify(userRepository).findByUsername("john_doe");

        Mockito.verify(userRepository, Mockito.never()).save(mockUser);
    }

}
