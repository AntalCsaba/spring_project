package com.example.springproject.dao;

public class AuthenticationResponse {

    public AuthenticationResponse() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private String token;

    public AuthenticationResponse(String token) {
        this.token = token;
    }
}
