package com.score.backend.models.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class FcmMessageRequest {
    @Schema(description = "유저 식별을 위한 고유 id 값")
    private Long userId;
    @Schema(description = "알림 제목")
    private String title;
    @Schema(description = "알림 내용")
    private String body;
}
