package com.example.jobApi.service.impl;

import com.example.jobApi.exception.CustomException;
import com.example.jobApi.model.User;
import com.example.jobApi.repository.UserRepository;
import com.example.jobApi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Override
    public User saveUser(User user){
        Optional<User> findUser = userRepository.findByUsername(user.getUsername());
        if(findUser.isPresent()){
            throw new CustomException("Username already exists", HttpStatus.BAD_REQUEST);
        }
        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        String encryptedPwd = bcrypt.encode(user.getPassword());
        user.setPassword(encryptedPwd);
        return userRepository.save(user);
    }

}
