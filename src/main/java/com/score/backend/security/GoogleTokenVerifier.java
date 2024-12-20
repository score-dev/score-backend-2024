package com.score.backend.security;

public class GoogleTokenVerifier extends AbstractTokenVerifier {
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
        return "";
    }
}
