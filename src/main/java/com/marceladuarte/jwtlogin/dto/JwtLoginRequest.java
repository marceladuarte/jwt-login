package com.marceladuarte.jwtlogin.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtLoginRequest {

    private String username;
    private String password;
    
}