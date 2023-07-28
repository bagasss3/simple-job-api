package com.example.jobApi.service;

import com.example.jobApi.dto.AuthenticationResponse;
import com.example.jobApi.exception.CustomException;
import com.example.jobApi.model.User;
import com.example.jobApi.repository.UserRepository;
import com.example.jobApi.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    public void testLogin_Successful() {
        String username = "testUser";
        String password = "test123";
        User savedUser = new User();
        User input = new User();

        savedUser.setUsername(username);
        input.setUsername(username);
        input.setPassword(password);

        String hashedPassword = "$2a$10$9YSGyH53xTbmVu1yNFcN3.5OjNu4Bd5bs510.v1lq0OuhBAruJEuG"; // Hashed version of "testPassword"
        savedUser.setPassword(hashedPassword);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(savedUser));

        String jwtToken = "mockedJwtToken";
        when(jwtService.generateToken(Mockito.any(), Mockito.eq(savedUser))).thenReturn(jwtToken);

        AuthenticationResponse response = authService.login(input);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(jwtToken, response.getToken());

        Mockito.verify(userRepository).findByUsername(username);

    }

    @Test
    public void testLogin_UserNotFound() {
        String username = "nonExistentUser";
        String password = "testPassword";
        User user = new User();

        user.setUsername(username);
        user.setPassword(password);

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> authService.login(user));

        Mockito.verify(userRepository).findByUsername(username);
    }

    @Test
    public void testLogin_WrongPassword() {
        String username = "testUser";
        String password = "wrongpass";
        User savedUser = new User();
        User input = new User();

        savedUser.setUsername(username);
        input.setUsername(username);
        input.setPassword(password);

        String hashedPassword = "$2a$10$9YSGyH53xTbmVu1yNFcN3.5OjNu4Bd5bs510.v1lq0OuhBAruJEuG"; // Hashed version of "testPassword"
        savedUser.setPassword(hashedPassword);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(savedUser));

        assertThrows(CustomException.class, () -> authService.login(input));

        Mockito.verify(userRepository).findByUsername(username);
    }
}
