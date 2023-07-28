package com.example.jobApi.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserTest {
    @Test
    public void UserTest(){
        Integer id = 1;
        String username = "test";
        String password = "pwdTest";

        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);

        Assertions.assertEquals(id, user.getId());
        Assertions.assertEquals(username, user.getUsername());
        Assertions.assertEquals(password, user.getPassword());
    }
}
