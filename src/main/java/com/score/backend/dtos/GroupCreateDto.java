package com.score.backend.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "그룹 생성 요청을 위한 DTO입니다.")
@Getter
@AllArgsConstructor
public class GroupCreateDto {

    @NotNull
    @Schema(description = "그룹을 생성하려고 하는 유저의 id 값")
    private Long adminId;

    @NotNull(message = "우리 그룹 이름은 무엇으로 해볼까요?")
    @Size(min = 1, max = 15)
    @Schema(description = "그룹명")
    private String groupName;

    @NotNull(message = "우리 그룹을 자유롭게 소개해보세요!")
    @Size(min = 1, max = 15)
    @Schema(description = "그룹 소개")
    private String groupDescription;

    @NotNull
    @Schema(description = "그룹에 가입 가능한 최대 멤버 수")
    private int userLimit;

    @NotNull
    @Schema(description = "비공개 그룹인지 여부")
    private boolean isPrivate;

    @Schema(description = "그룹 비밀번호 (공개 그룹인 경우 null)", nullable = true)
    private String groupPassword;
}
