package com.example.jobApi.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

@ExtendWith(MockitoExtension.class)
public class ApplicationConfigTest {
    @InjectMocks
    private ApplicationConfig applicationConfig;

    @Value("${jwt.secret_key}")
    private String secretKey;

    @Test
    public void ApplicationConfigTest(){
        Assertions.assertEquals(secretKey, applicationConfig.SECRET_KEY);
//        ApplicationConfig applicationConfig = new ApplicationConfig();
//
//        applicationConfig.setSECRET_KEY(secretKey);
//
//        Assertions.assertEquals(secretKey, applicationConfig.getSECRET_KEY());
    }
}
