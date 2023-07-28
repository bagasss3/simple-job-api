package com.example.jobApi.service.impl;

import com.example.jobApi.dto.AuthenticationResponse;
import com.example.jobApi.exception.CustomException;
import com.example.jobApi.model.User;
import com.example.jobApi.repository.UserRepository;
import com.example.jobApi.service.AuthService;
import com.example.jobApi.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtService jwtService;
    @Override
    public AuthenticationResponse login(User user){
        Optional<User> findUser = userRepository.findByUsername(user.getUsername());
        if(findUser.isEmpty()){
            throw new CustomException("user not found", HttpStatus.NOT_FOUND);
        }
        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        Boolean isMatch = bcrypt.matches(user.getPassword(), findUser.get().getPassword());
        if(!isMatch){
            throw new CustomException("Wrong password", HttpStatus.BAD_REQUEST);
        }
        String jwtToken = jwtService.generateToken(new HashMap<>(), findUser.get());
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setToken(jwtToken);
        return authenticationResponse;
    }
}
