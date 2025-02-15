package com.score.backend.dtos.schoolrank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Schema(description = "학교 랭킹 응답을 위한 DTO")
@Getter
@AllArgsConstructor
public class SchoolRankingResponse {
    @Schema(description = "학교명", example = "하남고등학교")
    private String schoolName;
    @Schema(description = "학교의 고유 ID 값", example = "1")
    private Long schoolId;
    @Schema(description = "해당 학교의 전체 랭킹 정보")
    private List<SchoolRankerResponse> allRankers;
    @Schema(description = "유저가 가입되어 있는 그룹에 대한 랭킹 정보")
    private List<SchoolRankerResponse> myGroupRanking;
}
