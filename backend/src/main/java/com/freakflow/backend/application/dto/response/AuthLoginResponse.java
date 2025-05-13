package com.freakflow.backend.application.dto.response;

public class AuthLoginResponse {
    public String token;
    public String tokenType;
    public long   expiresIn;
    public Long userId;
    public String name;
}
