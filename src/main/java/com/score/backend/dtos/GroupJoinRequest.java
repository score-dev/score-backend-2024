package com.score.backend.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "그룹 가입 신청 정보를 전송하기 위한 DTO입니다.")
@Getter
@AllArgsConstructor
public class GroupJoinRequest {
    @Schema(description = "그룹 가입 요청을 한 유저의 id 값")
    private Long requesterId;
    @Schema(description = "가입 요청을 한 그룹의 id 값")
    private Long groupId;
    @Schema(description = "그룹 가입 신청 메시지")
    private String message;
}
