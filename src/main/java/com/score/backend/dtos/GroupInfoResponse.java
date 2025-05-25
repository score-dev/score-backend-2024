package com.score.backend.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@Schema(description = "유저가 가입해 있지 않은 그룹에 대한 정보를 응답하는 DTO")
@NoArgsConstructor
@AllArgsConstructor
public class GroupInfoResponse {
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
    @Schema(description = "비공개 그룹인지 여부")
    private boolean isPrivate;
    @Schema(description = "그룹에 가입되어 있는 유저의 수")
    private int numOfTotalMembers;
    @Schema(description = "오늘 운동을 한 그룹원의 수")
    private int numOfExercisedToday;
    @Schema(description = "최근 피드 목록(가입해 있는 그룹에 대한 요청인 경우 모든 피드 정보 응답, 가입하지 않은 그룹에 대한 요청인 경우 피드 이미지만 응답.)")
    private Page<FeedInfoResponse> feeds;
}
