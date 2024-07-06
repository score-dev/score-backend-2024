package com.score.backend.models.dtos;

import com.score.backend.models.School;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.time.LocalTime;

@Schema(description = "회원 정보 수정을 위한 DTO")
@Getter
public class UserUpdateDto {
    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickname;

    @Schema(description = "학교")
    private School school;

    @Schema(description = "학년", example = "1")
    private int grade;

    @Schema(description = "키", maxLength = 10, nullable = true, example = "150")
    private int height;

    @Schema(description = "체중", maxLength = 10, nullable = true, example = "50")
    private int weight;

    @Schema(description = "목표 운동 시작 시간")
    private LocalTime goal;
}
