package com.marceladuarte.jwtlogin.service;

import java.time.Instant;
import java.util.Date;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtLoginService {

    protected static final String SECRET = "SeCrEtEsEcReTeSeCrEtEsEcReTeSeCrEtEsEcReTe";
    
    public String createToken(String username){
        return Jwts.builder()
                .setSubject("login")
                .setExpiration(Date.from(Instant.now().plusSeconds(3600)))                
                .claim("username", username)
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isValidToken(String token, String username) {
        try{
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .build()
                .parseClaimsJws(token).getBody();
            return claims.get("username").equals(username);
        }catch(JwtException e){
            return false;
        }
    }
}
