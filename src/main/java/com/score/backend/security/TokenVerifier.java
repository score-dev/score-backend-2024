package com.score.backend.security;

public interface TokenVerifier {
    boolean verify(String idToken);
}
