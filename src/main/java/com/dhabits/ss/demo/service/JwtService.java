package com.dhabits.ss.demo.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

public interface JwtService {

    String extractUsername(String token);

    Date extractExpiration(String token);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    Claims extractAllClaims(String token);

    boolean isTokenExpired(String token);

    boolean validateToken(String token, UserDetails userDetails);

    String GenerateToken(String username);

    String createToken(Map<String, Object> claims, String username);

    Key getSignKey();
}
