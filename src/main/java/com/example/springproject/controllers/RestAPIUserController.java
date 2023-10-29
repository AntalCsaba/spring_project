package com.example.springproject.controllers;

import com.example.springproject.dao.AuthenticationResponse;
import com.example.springproject.dtos.UserDTO;
import com.example.springproject.models.User;
import com.example.springproject.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

@RestController
public class RestAPIUserController {

    private final UserService userService;

    public RestAPIUserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/auth/addUser")
    public ResponseEntity<AuthenticationResponse> saveUser(@RequestBody UserDTO userDTO) throws NoSuchAlgorithmException {
       return ResponseEntity.ok(userService.register(userDTO.getUsername(), userDTO.getPassword()));
    }

    @PostMapping("/api/auth")
    public ResponseEntity<AuthenticationResponse> auth(@RequestBody UserDTO userDTO) throws NoSuchAlgorithmException {
        return ResponseEntity.ok(userService.authenticate(userDTO.getUsername(), userDTO.getPassword()));
    }


}
