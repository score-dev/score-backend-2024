package com.score.backend.models.dtos;

import com.score.backend.models.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalTime;

@Schema(description = "회원가입을 위한 DTO")
@Getter
@NoArgsConstructor(force = true)
public class UserDto {
    @Schema(description = "유저 닉네임(unique)", maxLength = 10, example = "김승주")
    private final String nickname;

    @Schema(description = "학년", example = "1")
    private final int grade;

    @Schema(description = "키", maxLength = 10, nullable = true, example = "150")
    private final int height;

    @Schema(description = "체중", maxLength = 10, nullable = true, example = "50")
    private final int weight;

    @Schema(description = "목표 운동 시작 시간")
    private final LocalTime goal;

    @Schema(description = "마케팅 푸시 수신 동의 여부")
    private final boolean marketing;

    @Schema(description = "기타 알림 수신 동의 여부")
    private final boolean push;

    @Schema(description = "provider id")
    private final String loginKey;

    @Builder
    public UserDto(String nickname, int grade, int height, int weight, LocalTime goal, boolean marketing, boolean push, String loginKey) {
        this.nickname = nickname;
        this.grade = grade;
        this.height = height;
        this.weight = weight;
        this.goal = goal;
        this.marketing = marketing;
        this.push = push;
        this.loginKey = loginKey;
    }

    public User toEntity() {
        return User.builder()
                .nickname(nickname)
                .grade(grade)
                .height(height)
                .weight(weight)
                .cumulativeTime(0.0)
                .level(1)
                .point(0)
                .consecutiveDate(0)
                .cumulativeDistance(0.0)
                .goal(goal)
                .marketing(marketing)
                .push(push)
                .loginKey(loginKey)
                .build();
    }
}
