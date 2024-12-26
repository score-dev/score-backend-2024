package com.score.backend.security;

import com.score.backend.domain.user.User;
import com.score.backend.domain.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@Tag(name = "Auth", description = "회원 인증을 처리하는 API입니다.")
@RequestMapping("/score/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthService authService;
    private final JwtProvider jwtProvider;

    @Operation(summary = "소셜 로그인 인증 완료 시 로그인 시도 검증 및 기존 회원 여부 확인", description = "발급된 id 토큰을 검증하고 기존 회원인지 여부를 확인합니다.")
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "소셜 로그인 인증 완료. Response Body가 true이면 기존 회원, false이면 신규 회원."),
                    @ApiResponse(responseCode = "400", description = "Bad Request")}
    )
    @RequestMapping(value = "/oauth", method = RequestMethod.GET)
    public ResponseEntity<Boolean> authorizeUser(@RequestParam @Parameter(required = true, description = "provider명 (google, kakao, apple)") String provider,
                                                 @RequestBody @Parameter(required = true, description = "provider가 발급한 id 토큰 값") String idToken) {
        if (userService.isPresentUser(authService.getUserId(provider, idToken))) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }

    @Operation(summary = "access token (재)발급 요청", description = "access token (재)발급 요청 시 refresh token을 검증한 후 발급을 진행합니다.")
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "access token 발급 완료"),
                    @ApiResponse(responseCode = "401", description = "refresh token이 만료되어 재로그인이 필요하거나 서명에 오류가 있습니다."),
                    @ApiResponse(responseCode = "404", description = "user not found")
            })
    @PostMapping("/refresh")
    public ResponseEntity<String> refreshToken(@RequestParam @Parameter(required = true, description = "유저의 고유 Id 값") Long userId) {
        try {
            User user = userService.findUserById(userId).get();
            String refreshToken = user.getRefreshToken();
            if (jwtProvider.validateToken(refreshToken)) {
                String accessToken = jwtProvider.createAccessToken(user.getLoginKey().toString());
                return ResponseEntity.ok(accessToken);
            }
            return ResponseEntity.status(401).build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).build();
        }
    }
}
