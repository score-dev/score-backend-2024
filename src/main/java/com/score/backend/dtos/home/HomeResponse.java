package com.score.backend.dtos.home;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Schema(description = "홈 화면 정보 전달을 위한 DTO")
@Getter
public class HomeResponse {
    @Schema(description = "유저의 닉네임")
    private String nickname;
    @Schema(description = "유저의 프로필 이미지 url")
    private String profileImgUrl;
    @Schema(description = "유저의 레벨")
    private int level;
    @Schema(description = "유저의 현재 포인트")
    private int point;
    @Schema(description = "유저의 이번주 각 요일별 운동 시간. (단위: 초, 순서대로 월요일~일요일)")
    private List<Double> weeklyExerciseTimeByDay;
    @Schema(description = "유저가 이번 주 운동한 총 시간 (단위: 초)")
    private double weeklyTotalExerciseTime;
    @Schema(description = "유저가 이번주에 3분 이상 운동한 날짜 수")
    private int weeklyExerciseCount;
    @Schema(description = "유저의 연속 운동 날짜 수")
    private int consecutiveDate;
    @Schema(description = "유저가 가입한 그룹의 수")
    private int numOfGroups;
    @Schema(description = "유저가 가입한 각 그룹에 대한 정보를 전달하는 DTO")
    private List<HomeGroupInfoResponse> groupsInfo;

    public HomeResponse(String nickname, String profileImgUrl, int level, int point,
                        List<Double> weeklyExerciseTimeByDay, double weeklyTotalExerciseTime, int weeklyExerciseCount, int consecutiveDate, int numOfGroups, List<HomeGroupInfoResponse> groupsInfo) {
        this.nickname = nickname;
        this.profileImgUrl = profileImgUrl;
        this.level = level;
        this.point = point;
        this.weeklyExerciseTimeByDay = weeklyExerciseTimeByDay;
        this.weeklyTotalExerciseTime = weeklyTotalExerciseTime;
        this.weeklyExerciseCount = weeklyExerciseCount;
        this.consecutiveDate = consecutiveDate;
        this.numOfGroups = numOfGroups;
        this.groupsInfo = groupsInfo;
    }
}
