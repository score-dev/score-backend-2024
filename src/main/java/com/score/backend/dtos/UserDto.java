package com.score.backend.dtos;

import com.score.backend.domain.user.User;
import com.score.backend.domain.user.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalTime;

@Schema(description = "회원가입을 위한 DTO")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDto {
    @NotBlank(message = "닉네임을 입력해주세요.")
    @Length(max = 10, message = "닉네임은 10자 이내로 입력해야 합니다.")
    @Schema(description = "유저 닉네임(unique)", maxLength = 10, example = "김승주")
    private String nickname;

    @Schema(description = "학년", example = "1")
    private int grade;

    @Schema(description = "키", maxLength = 10, nullable = true, example = "150")
    private int height;

    @Schema(description = "체중", maxLength = 10, nullable = true, example = "50")
    private int weight;

    @Schema(description = "성별", nullable = true, example = "FEMALE")
    private Gender gender;

    @Schema(description = "목표 운동 시작 시간")
    private LocalTime goal;

    @Schema(description = "마케팅 푸시 수신 동의 여부")
    private boolean marketing;

    @Schema(description = "목표 운동 시간 알림 수신 동의 여부")
    private boolean exercisingTime;

    @Schema(description = "소셜 로그인 Provider(google, kakao, apple)")
    private String provider;

    @Schema(description = "소셜 로그인 provider가 제공한 ID Token")
    private String idToken;

    public User toEntity(Long sub) {
        return User.builder()
                .nickname(nickname)
                .grade(grade)
                .height(height)
                .weight(weight)
                .gender(gender)
                .totalCumulativeTime(0.0)
                .weeklyCumulativeTime(0.0)
                .weeklyExerciseCount(0)
                .weeklyLevelIncrement(0)
                .level(1)
                .point(0)
                .consecutiveDate(0)
                .cumulativeDistance(0.0)
                .goal(goal)
                .marketing(marketing)
                .tag(true)
                .exercisingTime(exercisingTime)
                .loginKey(sub)
                .build();
    }
}
