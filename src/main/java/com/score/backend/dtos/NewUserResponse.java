package com.score.backend.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import java.util.List;

@Schema(description = "회원가입이 완료된 유저의 정보를 응답하기 위한 DTO")
@Getter
public class NewUserResponse {
    @Schema(description = "유저의 고유 id 값")
    private Long id;
    @Schema(description = "발급된 JWT access token(1시간 유효).")
    private String accessToken;
    @Schema(description = "발급된 JWT refresh token(2주 유효).")
    private String refreshToken;

    public NewUserResponse(Long id, List<String> token) {
        this.id = id;
        this.accessToken = token.get(0);
        this.refreshToken = token.get(1);
    }
}
