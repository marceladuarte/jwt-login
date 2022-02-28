package com.marceladuarte.jwtlogin.controller;

import javax.servlet.http.HttpServletRequest;

import com.marceladuarte.jwtlogin.dto.JwtLoginRequest;
import com.marceladuarte.jwtlogin.dto.JwtLoginResponse;
import com.marceladuarte.jwtlogin.service.JwtLoginService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class JwtLoginController {

    protected static final String USERNAME = "johndoe";
    protected static final String PASSWORD = "1234";

    @Autowired
    private JwtLoginService service;
    
    @PostMapping("/login")
    public JwtLoginResponse login(@RequestBody JwtLoginRequest credentials){
        // hard-coded "authentication"
        if(!USERNAME.equals(credentials.getUsername()) || !PASSWORD.equals(credentials.getPassword())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid creadentials. Try again.");
        }

        JwtLoginResponse response = new JwtLoginResponse();
        response.setToken(service.createToken(credentials.getUsername()));
        return response;
    }

    @GetMapping("/logged")
    public ResponseEntity<String> isLogged(HttpServletRequest request){
        var auth = request.getHeader("AUTHORIZATION");
        if(auth == null || !auth.startsWith("Bearer ")){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not logged. Invalid token.");
        }
        
        // extract token
        var token = auth.split(" ")[1];
        if(!service.isValidToken(token, USERNAME)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not logged. Invalid token.");
        }

        return new ResponseEntity<>("You're logged!\n", HttpStatus.OK);
    }
}
