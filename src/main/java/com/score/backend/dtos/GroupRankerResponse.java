package com.score.backend.dtos;

import com.score.backend.domain.rank.group.GroupRanker;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "그룹 메이트들의 랭킹 정보 응답을 위한 DTO")
public class GroupRankerResponse {
    @Schema(description = "유저를 식별할 수 있는 고유 id")
    private Long userId;
    @Schema(description = "유저의 닉네임")
    private String nickname;
    @Schema(description = "유저의 프로필 이미지 URL")
    private String profileImgUrl;
    @Schema(description = "유저의 주간 등수")
    private int rankNum;
    @Schema(description = "지난주와의 등수차(3이면 3등 상승했다는 뜻, -2이면 2등 하락했다는 뜻.)")
    private int changedAmount;
    @Schema(description = "주간 레벨 상승 현황")
    private int weeklyLevelIncrement;
    @Schema(description = "주간 운동 시간 현황")
    private double weeklyExerciseTime;

    public GroupRankerResponse(GroupRanker groupRanker) {
        this.userId = groupRanker.getUser().getId();
        this.nickname = groupRanker.getUser().getNickname();
        this.profileImgUrl = groupRanker.getUser().getProfileImg();
        this.rankNum = groupRanker.getRankNum();
        this.changedAmount = groupRanker.getChangedAmount();
        this.weeklyLevelIncrement = groupRanker.getWeeklyLevelIncrement();
        this.weeklyExerciseTime = groupRanker.getWeeklyExerciseTime();
    }
}
