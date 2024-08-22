package com.score.backend.models.dtos;

import com.score.backend.models.exercise.Exercise;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;

@Schema(description = "유저가 가입해 있는 그룹에 대한 정보를 응답하는 DTO")
@AllArgsConstructor
public class MemberGroupInfoResponse {
    @Schema(description = "공개 그룹인지 여부")
    private boolean isPrivate;
    @Schema(description = "그룹 명")
    private String groupName;
    @Schema(description = "그룹에 가입되어 있는 유저의 수")
    private int numOfTotalMembers;
    @Schema(description = "오늘 운동을 한 그룹원의 수")
    private int numOfExercisedToday;
    @Schema(description = "최근 피드 목록")
    private Page<Exercise> feeds;
}
