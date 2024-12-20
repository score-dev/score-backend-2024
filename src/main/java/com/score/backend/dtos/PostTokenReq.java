package com.score.backend.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Schema(description = "사용자 토큰을 받기 위한 DTO입니다.")
@Getter
public class PostTokenReq {
    @NotBlank(message="토큰을 입력해야 합니다.")
    @Schema(description = "FCM Token", example = "")
    String token;
}