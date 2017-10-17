package com.sevya.launchpad.security.jwt;

import static java.time.ZoneOffset.UTC;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtService {
    private static final String ISSUER = "in.sevya.jwt";
    private SecretKeyProvider secretKeyProvider;

    @SuppressWarnings("unused")
    public JwtService() {
        this(null);
    }

    @Autowired
    public JwtService(SecretKeyProvider secretKeyProvider) {
        this.secretKeyProvider = secretKeyProvider;
    }
    
    public JwtAuthenticatedUserDto verify(String token) throws IOException, URISyntaxException {
    	
        byte[] secretKey = secretKeyProvider.getKey();
        Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        JwtAuthenticatedUserDto jwtAuthenticatedUserDto = new JwtAuthenticatedUserDto();
        jwtAuthenticatedUserDto.setMobile(claims.getBody().getSubject().toString());
        jwtAuthenticatedUserDto.setUserId(Long.parseLong(claims.getBody().getId()));
        return jwtAuthenticatedUserDto;
    }

    public String tokenFor(JwtAuthenticatedUserDto authenticatedUserDto) throws IOException, URISyntaxException {
    	
        byte[] secretKey = secretKeyProvider.getKey();
        Date expiration = Date.from(LocalDateTime.now().plusHours(2).toInstant(UTC));
        
        return Jwts.builder()
                .setSubject(authenticatedUserDto.getMobile())
                .setId(authenticatedUserDto.getUserId().toString())
                .setExpiration(expiration)
                .setIssuer(ISSUER)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }
}