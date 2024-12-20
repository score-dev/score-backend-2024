package com.score.backend.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FcmMessageRequest {
    @Schema(description = "유저 식별을 위한 고유 id 값")
    private Long userId;
    @Schema(description = "알림 제목")
    private String title;
    @Schema(description = "알림 내용")
    private String body;
}
