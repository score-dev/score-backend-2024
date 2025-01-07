package com.score.backend.security.oauth;

import org.springframework.beans.factory.annotation.Value;

public class KakaoTokenVerifier extends AbstractTokenVerifier {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;

    @Override
    protected String getJwkUrl() {
        return "https://kauth.kakao.com/.well-known/jwks.json";
    }

    @Override
    protected String getIssuer() {
        return "https://kauth.kakao.com";
    }

    @Override
    protected String getClientId() {
        return kakaoClientId;
    }
}
