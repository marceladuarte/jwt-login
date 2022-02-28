package com.marceladuarte.jwtlogin.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JwtLoginServiceTest {

    @InjectMocks
    JwtLoginService jwtLoginService;

    @BeforeEach
    void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createToken_success(){
        var username = "randomuser";
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(JwtLoginService.SECRET.getBytes()))
                .build()
                .parseClaimsJws(jwtLoginService.createToken(username)).getBody();
        assertEquals(username, claims.get("username"));
    }

    @Test
    void isValidToken_invalidUsername(){
        var token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsb2dpbiIsImV4cCI6MTY0NjAxMjA4NywidXNlcm5hbWUiOiJyYW5kb211c2VyIn0.Vifqctv2KQruYhEyexmMMVmyxM2UIrOCDVaksSfwMK4";
        assertFalse(jwtLoginService.isValidToken(token, "anyuser"));
    }

    @Test
    void isValidToken_expiredToken(){
        var token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsb2dpbiIsImV4cCI6MTY0NjAxMjA4NywidXNlcm5hbWUiOiJyYW5kb211c2VyIn0.Vifqctv2KQruYhEyexmMMVmyxM2UIrOCDVaksSfwMK4";
        assertFalse(jwtLoginService.isValidToken(token, "randomuser"));
    }

    @Test
    void isValidToken_success(){
        var token = jwtLoginService.createToken("randomuser");
        assertTrue(jwtLoginService.isValidToken(token, "randomuser"));
    }    
}