package com.example.challenge_mottu_java.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import com.example.challenge_mottu_java.Enums.RolesUser;
import com.example.challenge_mottu_java.dto.UserDTO;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import com.example.challenge_mottu_java.model.Token;
import com.example.challenge_mottu_java.model.User;

@Service
public class TokenService {
    private Algorithm algorithm = Algorithm.HMAC256("secret");

    public Token createToken(User user){
        var jwt = JWT.create()
                .withSubject(user.getId().toString())
                .withClaim("email", user.getUsername())
                .withClaim("role", user.getRole().toString())
                .sign(algorithm);

        return new Token(jwt, user.getUsername(), user.getRole(), user.getCourt().courtToDTO());


    }

    public User getUserFromToken(String jwt) {
        var jwtVerified = JWT.require(algorithm).build().verify(jwt);
        return User.builder()
                .id(Long.valueOf(jwtVerified.getSubject()))
                .username(jwtVerified.getClaim("username").toString())
                .role(RolesUser.valueOf(jwtVerified.getClaim("role").asString()))
                .build();

    }

}