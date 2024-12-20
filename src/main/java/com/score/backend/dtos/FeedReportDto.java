package com.score.backend.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "피드 신고를 위한 DTO")
@Getter
public class FeedReportDto {
    private Long agentId;
    private Long feedId;
    private String reportReason;
    private String comment;
}
