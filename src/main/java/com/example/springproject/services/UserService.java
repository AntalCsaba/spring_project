package com.example.springproject.services;

import com.example.springproject.dao.AuthenticationResponse;
import com.example.springproject.models.User;
import com.example.springproject.repositories.UserRepository;
import com.example.springproject.security.JwtService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }
    private static String getHash(String password) throws NoSuchAlgorithmException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            String part = Integer.toHexString((b & 0xff) + 0x100).substring(1);
            sb.append(part);
        }

        return sb.toString();
    }

    public AuthenticationResponse register(String username, String password) throws NoSuchAlgorithmException {
        Optional<User> oldUser = userRepository.findUserByUsername(username);
        if (oldUser.isPresent())
            throw new IllegalArgumentException("User with that name already exists.");

        String passwordHash = getHash(password);
        User user = new User(username, passwordHash);
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return new AuthenticationResponse(jwtToken);
    }

    public AuthenticationResponse authenticate(String email, String password) throws NoSuchAlgorithmException, AccessDeniedException {
        String hash = getHash(password);

        Optional<User> user = userRepository.findUserByUsername(email);
        if (user.isEmpty())
            throw new AccessDeniedException("User was not found.");

        if (!user.get().getPassword().equals(hash))
            throw new AccessDeniedException("Invalid password!");

        var jwtToken = jwtService.generateToken(user.get());
        return new AuthenticationResponse(jwtToken);
    }

}
