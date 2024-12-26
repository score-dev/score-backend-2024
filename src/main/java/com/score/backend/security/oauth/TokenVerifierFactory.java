package com.score.backend.security.oauth;

public class TokenVerifierFactory {
    public static TokenVerifier getVerifier(String provider) {
        return switch (provider.toLowerCase()) {
            case "google" -> new GoogleTokenVerifier();
            case "apple" -> new AppleTokenVerifier();
            case "kakao" -> new KakaoTokenVerifier();
            default -> throw new IllegalArgumentException("Unknown provider: " + provider);
        };
    }
}
