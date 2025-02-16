package com.score.backend.dtos.schoolrank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "학교 랭킹에 있는 각 그룹에 대한 정보를 전달하기 위한 DTO")
@Getter
@AllArgsConstructor
public class SchoolRankerResponse {
    @Schema(description = "해당 그룹의 고유 ID 값", example = "1")
    private Long groupId;
    @Schema(description = "그룹명")
    private String groupName;
    @Schema(description = "그룹 프로필 사진 URL")
    private String groupImg;
    @Schema(description = "비공개 그룹 여부")
    private boolean isPrivate;
    @Schema(description = "그룹에 가입 가능한 유저 수")
    private int maxMemberNum;
    @Schema(description = "현재 해당 그룹에 가입해 있는 유저 수")
    private int currentMemberNum;
    @Schema(description = "해당 그룹의 등수")
    private int rank;
    @Schema(description = "전 주차와의 등수차")
    private int rankChangeAmount;
    @Schema(description = "그룹의 참여율")
    private double participateRatio;
    @Schema(description = "그룹의 총 운동 시간")
    private double weeklyExerciseTime;

    public SchoolRankerResponse(Long groupId, String groupName, String groupImg, int rank, double participateRatio, double weeklyExerciseTime) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupImg = groupImg;
        this.rank = rank;
        this.participateRatio = participateRatio;
        this.weeklyExerciseTime = weeklyExerciseTime;
    }
}
