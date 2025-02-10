package com.score.backend.security;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.score.backend.domain.user.User;
import com.score.backend.domain.user.UserService;
import com.score.backend.security.oauth.TokenVerifier;
import com.score.backend.security.oauth.TokenVerifierFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtProvider jwtProvider;
    private boolean verifyToken(String provider, String idToken) {
        TokenVerifier verifier = TokenVerifierFactory.getVerifier(provider);
        return verifier.verify(idToken);
    }

    public String getUserId(String provider, String idToken) {
        try {
            if (verifyToken(provider, idToken)) {
                SignedJWT signedJWT = SignedJWT.parse(idToken);
                JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
                log.info(claims.getSubject());
                return claims.getSubject();
            }
        } catch (ParseException e) {
            throw new RuntimeException("Failed to extract user info", e);
        }
        return "";
    }

    @Transactional
    public List<String> setJwtToken(String provider, String idToken) {
        String userId = getUserId(provider, idToken);
        User user = userService.findUserByLoginKey(userId).orElseThrow(
                () -> new RuntimeException("User not found")
        );
        List<String> tokens = jwtProvider.getNewToken(userId);
        user.setRefreshToken(tokens.get(1));

        return tokens;
    }
}
