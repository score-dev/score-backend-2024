package com.score.backend.security.oauth;

public class KakaoTokenVerifier extends AbstractTokenVerifier {
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
        return "";
    }
}
