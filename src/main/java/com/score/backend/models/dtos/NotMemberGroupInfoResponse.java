package com.score.backend.models.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;

@Schema(description = "유저가 가입해 있지 않은 그룹에 대한 정보를 응답하는 DTO")
@AllArgsConstructor
public class NotMemberGroupInfoResponse {
    // 학교 랭킹 추가 필요
    @Schema(description = "그룹명")
    private String groupName;
    @Schema(description = "그룹에 가입 가능한 최대 멤버 수")
    private int userLimit;
    @Schema(description = "그룹의 누적 운동 시간(단위: 초)")
    private double cumulativeTime;
    @Schema(description = "그룹의 지난 주 참여율")
    private double averageParticipateRatio;
    @Schema(description = "그룹의 프로필 이미지 URL")
    private String groupImg;
    @Schema(description = "피드 이미지 목록(가입해있지 않은 그룹이므로 피드 내용은 보이지 않음.)")
    private Page<String> feedImgs;
}