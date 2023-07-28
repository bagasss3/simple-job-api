package com.example.jobApi.controller;

import com.example.jobApi.dto.ApiResponse;
import com.example.jobApi.dto.AuthenticationResponse;
import com.example.jobApi.exception.CustomException;
import com.example.jobApi.model.User;
import com.example.jobApi.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@Valid @RequestBody User user){
        try{
            AuthenticationResponse token = authService.login(user);
            ApiResponse<AuthenticationResponse> response = new ApiResponse<>();
            response.setSuccess(true);
            response.setMessage("Success login");
            response.setData(token);
            return ResponseEntity.ok(response);
        } catch (CustomException e) {
            ApiResponse<Void> response = new ApiResponse<>();
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            return ResponseEntity.status(e.getHttpStatus()).body(response);
        } catch (Exception e) {
            ApiResponse<Void> response = new ApiResponse<>();
            response.setSuccess(false);
            response.setMessage("Error message: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
