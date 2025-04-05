package com.score.backend.dtos;

import com.score.backend.domain.rank.group.GroupRanking;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Schema(description = "그룹의 주간 랭킹 응답을 위한 DTO")
public class GroupRankingResponse {
    @Schema(description = "그룹을 식별할 수 있는 고유 id")
    private Long groupId;
    @Schema(description = "응답된 주간 랭킹의 산정이 시작된 날짜(월요일)")
    private LocalDate startDate;
    @Schema(description = "응답된 주간 랭킹의 산정이 마감된 날짜(일요일)")
    private LocalDate endDate;
    @Schema(description = "각 그룹 메이트들의 랭킹 정보")
    private List<GroupRankerResponse> rankersInfo;

    public GroupRankingResponse(GroupRanking groupRanking, List<GroupRankerResponse> rankersInfo) {
        this.groupId = groupRanking.getGroup().getGroupId();
        this.startDate = groupRanking.getStartDate();
        this.endDate = groupRanking.getEndDate();
        this.rankersInfo = rankersInfo;
    }
}
