package com.score.backend.security.oauth;

public class AppleTokenVerifier extends AbstractTokenVerifier {
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
        return "";
    }
}
