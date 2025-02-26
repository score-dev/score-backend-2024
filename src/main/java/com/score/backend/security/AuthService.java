package com.score.backend.security;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.score.backend.domain.user.User;
import com.score.backend.domain.user.UserService;
import com.score.backend.security.oauth.TokenVerifier;
import com.score.backend.security.oauth.TokenVerifierFactory;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

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

    public String getUserId(String provider, String idToken) throws ParseException {
        if (verifyToken(provider, idToken)) {
            SignedJWT signedJWT = SignedJWT.parse(idToken);
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            log.info(claims.getSubject());
            return claims.getSubject();
        }
        throw new JwtException("id 토큰 검증에 실패하였습니다.");
    }

    @Transactional
    public List<String> setJwtToken(String provider, String idToken) throws ParseException {
        String userId = getUserId(provider, idToken);
        User user = userService.findUserByLoginKey(userId).orElseThrow(
                () -> new NoSuchElementException("일치하는 유저 정보가 존재하지 않습니다.")
        );
        List<String> tokens = jwtProvider.getNewToken(userId);
        user.setRefreshToken(tokens.get(1));

        return tokens;
    }
}
