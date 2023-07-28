package com.example.jobApi.service.impl;

import com.example.jobApi.config.ApplicationConfig;
import com.example.jobApi.exception.CustomException;
import com.example.jobApi.service.JwtService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {
    @Autowired
    ApplicationConfig applicationConfig;
    @Override
    public String extractUsername(String token) {
        try {
            return extractClaim(token, Claims::getSubject);
        } catch (ExpiredJwtException ex) {
            throw new CustomException("Token has expired", HttpStatus.UNAUTHORIZED);
        } catch (JwtException ex) {
            throw new CustomException("Invalid token", HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public Boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        boolean isValid = username.equals(userDetails.getUsername()) && !isTokenExpired(token);
        if(!isValid){
            throw new CustomException("Token is not valid", HttpStatus.UNAUTHORIZED);
        }
        return true;
    }

    @Override
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    private Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }
    private Key getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(applicationConfig.SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    public Boolean isTokenExpired(String token){
        boolean isExpired = extractExpiration(token).before(new Date());
        if(isExpired){
            throw new CustomException("Token already expired", HttpStatus.UNAUTHORIZED);
        }
        return false;
    }

}
