package com.score.backend.models.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "유저 신고를 위한 DTO")
@Getter
public class UserReportDto {
    private Long agentId;
    private Long objectId;
    private String reportReason;
    private String comment;
}
