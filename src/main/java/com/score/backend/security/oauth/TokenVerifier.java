package com.score.backend.security.oauth;

public interface TokenVerifier {
    boolean verify(String idToken);
}
