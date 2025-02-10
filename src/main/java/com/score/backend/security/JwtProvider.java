package com.score.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtProvider {
    private final long ACCESS_TOKEN_VALID_MILISECOND = 1000L * 60 * 60; // 1시간
    private final long REFRESH_TOKEN_VALID_MILISECOND = 1000L * 60 * 60 * 24 * 14; // 2주

    @Value("${custom.jwt.secretKey}")
    private String plainSecretKey;
    private SecretKey cachedSecretKey;


    // plain key to secret key
    private SecretKey _getSecretKey() {
        String keyBase64Encoded = Base64.getEncoder().encodeToString(plainSecretKey.getBytes());
        return Keys.hmacShaKeyFor(keyBase64Encoded.getBytes());
    }
    public SecretKey getSecretKey() {
        if (cachedSecretKey == null) {
            cachedSecretKey = _getSecretKey();
        }
        return cachedSecretKey;
    }

    public List<String> getNewToken(String userKey) {
        List<String> tokens = new ArrayList<>();
        tokens.add(createAccessToken(userKey));
        tokens.add(createRefreshToken(userKey));
        return tokens;
    }

    // access token 생성
    public String createAccessToken(String userKey) {
        Claims claims = Jwts.claims().setSubject(userKey);
        Date presentDate = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(presentDate)
                .setExpiration(new Date(presentDate.getTime() + ACCESS_TOKEN_VALID_MILISECOND))
                .signWith(getSecretKey())
                .compact();
    }

    // access token 만료시 갱신을 위한 refresh token 생성
    public String createRefreshToken(String userKey) {
        Claims claims = Jwts.claims().setSubject(userKey);
        Date presentDate = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(presentDate)
                .setExpiration(new Date(presentDate.getTime() + REFRESH_TOKEN_VALID_MILISECOND))
                .signWith(getSecretKey())
                .compact();
    }

    // refresh token 검증
    public boolean validateToken(String refreshToken) {
        try {
            Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(refreshToken);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
