package com.score.backend.security;

import com.score.backend.domain.user.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
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
    private final long ACCESS_TOKEN_VALID_MILISECOND = 1000L * 60 * 60;
    private final long REFRESH_TOKEN_VALID_MILISECOND = 1000L * 60 * 60 * 24 * 14;

    @Value("${custom.jwt.secretKey}")
    private String plainSecretKey;
    private SecretKey cachedSecretKey;
    private final UserService userService;


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

    public List<String> getNewToken(String token) {
        List<String> tokens = new ArrayList<>();
        tokens.add(createAccessToken(token));
        tokens.add(createRefreshToken());
        return tokens;
    }

    // 토큰으로부터 세부 정보(claims) 얻기
    public Jws<Claims> getClaimsFromJwt(String token) {
        return Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(token);
    }

    //////////// 백엔드 코드에 있는 모든 'access token'은 소셜 로그인 인증 과정에서 사용되는 access token이 아니라 소셜 로그인 인증 완료 이후 발급하는 자체 jwt 토큰입니다! ////////////

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
    private String createRefreshToken() {
        Claims claims = Jwts.claims().setSubject("RefreshToken");
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
            return Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(refreshToken).getBody().getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
