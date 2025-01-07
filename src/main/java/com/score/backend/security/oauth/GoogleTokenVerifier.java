package com.score.backend.security.oauth;

import org.springframework.beans.factory.annotation.Value;

public class GoogleTokenVerifier extends AbstractTokenVerifier {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Override
    protected String getJwkUrl() {
        return "https://www.googleapis.com/oauth2/v3/certs";
    }

    @Override
    protected String getIssuer() {
        return "https://accounts.google.com";
    }

    @Override
    protected String getClientId() {
        return googleClientId;
    }
}
