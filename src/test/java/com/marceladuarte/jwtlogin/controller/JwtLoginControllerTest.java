package com.marceladuarte.jwtlogin.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;

import com.marceladuarte.jwtlogin.dto.JwtLoginRequest;
import com.marceladuarte.jwtlogin.dto.JwtLoginResponse;
import com.marceladuarte.jwtlogin.service.JwtLoginService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

public class JwtLoginControllerTest {

    @Mock
    JwtLoginService jwtLoginService;

    @Mock
    JwtLoginRequest loginRequest;

    @Mock
    HttpServletRequest servletRequest;

    @InjectMocks
    JwtLoginController jwtLoginController;

    @BeforeEach
	void init() {
		MockitoAnnotations.initMocks(this);
	}

    @Test
    void login_sucess(){
        String expectedToken = "token.token.token";
        when(loginRequest.getUsername()).thenReturn(JwtLoginController.USERNAME);
        when(loginRequest.getPassword()).thenReturn(JwtLoginController.PASSWORD);

        when(jwtLoginService.createToken(JwtLoginController.USERNAME)).thenReturn(expectedToken);
        JwtLoginResponse response = jwtLoginController.login(loginRequest); 
        assertEquals(expectedToken, response.getToken());
    }

    @Test
    void login_invalidUsername(){
        when(loginRequest.getUsername()).thenReturn(null);
        when(loginRequest.getPassword()).thenReturn(JwtLoginController.PASSWORD);

        when(jwtLoginService.createToken(JwtLoginController.USERNAME)).thenReturn("");
        try{
            jwtLoginController.login(loginRequest);
            fail("Should throw ResponseStatusException");
        }catch(ResponseStatusException e){
            assertTrue(e.getMessage().contains("Invalid creadentials. Try again."));
        }
    }

    @Test
    void login_invalidPassword(){
        when(loginRequest.getUsername()).thenReturn(JwtLoginController.USERNAME);
        when(loginRequest.getPassword()).thenReturn("any password");

        when(jwtLoginService.createToken(JwtLoginController.USERNAME)).thenReturn("");
        try{
            jwtLoginController.login(loginRequest);
            fail("Should throw ResponseStatusException");
        }catch(ResponseStatusException e){
            assertTrue(e.getMessage().contains("Invalid creadentials. Try again."));
        }
    }

    @Test
    void isLogged_nullToken(){
        when(servletRequest.getHeader("AUTHORIZATION")).thenReturn(null);
        try{
            jwtLoginController.isLogged(servletRequest);
            fail("Should throw ResponseStatusException");
        }catch(ResponseStatusException e){
            assertTrue(e.getMessage().contains("Not logged. Invalid token."));
        }
    }

    @Test
    void isLogged_invalidToken(){
        String token = "Bearer token.token.token";
        when(servletRequest.getHeader("AUTHORIZATION")).thenReturn(token);
        when(jwtLoginService.isValidToken("token.token.token", JwtLoginController.USERNAME)).thenReturn(false);

        try{
            jwtLoginController.isLogged(servletRequest);
            fail("Should throw ResponseStatusException");
        }catch(ResponseStatusException e){
            assertTrue(e.getMessage().contains("Not logged. Invalid token."));
        }
    }

    @Test
    void isLogged_success(){
        String token = "Bearer token.token.token";
        when(servletRequest.getHeader("AUTHORIZATION")).thenReturn(token);
        when(jwtLoginService.isValidToken("token.token.token", JwtLoginController.USERNAME)).thenReturn(true);
        assertEquals("You're logged!\n", jwtLoginController.isLogged(servletRequest).getBody());
    }
}
