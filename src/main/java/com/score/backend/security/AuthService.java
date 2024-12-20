package com.score.backend.security;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
public class AuthService {
    private boolean verifyToken(String provider, String idToken) {
        TokenVerifier verifier = TokenVerifierFactory.getVerifier(provider);
        return verifier.verify(idToken);
    }

    public Long getUserId(String provider, String idToken) {
        try {
            if (verifyToken(provider, idToken)) {
                SignedJWT signedJWT = SignedJWT.parse(idToken);
                JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
                return Long.parseLong(claims.getSubject());
            }
        } catch (ParseException e) {
            throw new RuntimeException("Failed to extract user info", e);
        }
        return 0L;
    }
}
