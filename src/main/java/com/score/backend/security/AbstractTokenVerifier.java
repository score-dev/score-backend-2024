package com.score.backend.security;

import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.net.URL;
import java.util.Date;

public abstract class AbstractTokenVerifier implements TokenVerifier {

    protected abstract String getJwkUrl();
    protected abstract String getIssuer();
    protected abstract String getClientId();

    @Override
    public boolean verify(String idToken) {
        try {
            // JWK 키셋 로드
            URL jwkUrl = new URL(getJwkUrl());
            JWKSet jwkSet = JWKSet.load(jwkUrl);

            // JWT 파싱
            SignedJWT signedJWT = SignedJWT.parse(idToken);
            JWK jwk = jwkSet.getKeyByKeyId(signedJWT.getHeader().getKeyID());

            if (jwk == null) {
                throw new RuntimeException("No matching JWK found.");
            }

            // 서명 검증
            RSASSAVerifier verifier = new RSASSAVerifier(jwk.toRSAKey());
            if (!signedJWT.verify(verifier)) {
                throw new RuntimeException("Signature verification failed.");
            }

            // Claims 검증
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            if (!claims.getIssuer().equals(getIssuer())) {
                throw new RuntimeException("Invalid issuer.");
            }

            if (!claims.getAudience().contains(getClientId())) {
                throw new RuntimeException("Invalid audience.");
            }

            if (claims.getExpirationTime().before(new Date())) {
                throw new RuntimeException("Token expired.");
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
