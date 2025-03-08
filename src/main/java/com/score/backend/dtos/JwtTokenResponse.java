package com.score.backend.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import java.util.List;

@Schema(description = "인증이 완료된 유저의 id 값과 JWT 토큰 정보를 응답하기 위한 DTO")
@Getter
public class JwtTokenResponse {
    @Schema(description = "DB에 정보가 저장된 회원이라면 유저의 고유 id 값, 저장되어 있지 않은 회원이라면 -1")
    private Long id;
    @Schema(description = "DB에 정보가 저장된 회원이라면 새롭게 발급된 JWT access token(1시간 유효), 저장되어 있지 않은 회원이라면 \"New User\"")
    private String accessToken;
    @Schema(description = "DB에 정보가 저장된 회원이라면 새롭게 발급된 JWT refresh token(2주 유효), 저장되어 있지 않은 회원이라면 \"New User\"")
    private String refreshToken;

    public JwtTokenResponse(Long id, List<String> token) {
        this.id = id;
        this.accessToken = token.get(0);
        this.refreshToken = token.get(1);
    }
}
