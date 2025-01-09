package com.score.backend.security.oauth;

import org.springframework.beans.factory.annotation.Value;

public class AppleTokenVerifier extends AbstractTokenVerifier {

    @Value("${spring.security.oauth2.client.registration.apple.client-id}")
    private String appleClientId;

    @Override
    protected String getJwkUrl() {
        return "https://appleid.apple.com/auth/keys";
    }

    @Override
    protected String getIssuer() {
        return "https://appleid.apple.com";
    }

    @Override

    protected String getClientId() {
        return appleClientId;
    }
}
