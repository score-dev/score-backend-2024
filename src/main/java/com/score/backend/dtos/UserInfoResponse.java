package com.score.backend.dtos;

import com.score.backend.domain.user.Gender;
import com.score.backend.domain.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;

@Schema(description = "유저 정보 응답을 위한 DTO")
@Getter
@AllArgsConstructor
public class UserInfoResponse {
    @Schema(description = "유저의 고유 id 값")
    private Long userId;
    @Schema(description = "유저의 닉네임")
    private String nickname;
    @Schema(description = "유저의 성별")
    private Gender gender;
    @Schema(description = "유저의 학교 고유 id 값")
    private Long schoolId;
    @Schema(description = "유저의 학교명")
    private String schoolName;
    @Schema(description = "유저의 학년")
    private int grade;
    @Schema(description = "유저의 키")
    private int height;
    @Schema(description = "유저의 체중")
    private int weight;
    @Schema(description = "유저의 프로필 이미지 url")
    private String profileImgUrl;
    @Schema(description = "유저의 목표 운동 시간")
    private LocalTime goal;
    @Schema(description = "유저의 현재 레벨")
    private int level;
    @Schema(description = "유저의 현재 포인트 정보")
    private int point;

    public static UserInfoResponse of(User user) {
        return new UserInfoResponse(
                user.getId(),
                user.getNickname(),
                user.getGender(),
                user.getSchool().getId(),
                user.getSchool().getSchoolName(),
                user.getGrade(),
                user.getHeight(),
                user.getWeight(),
                user.getProfileImg(),
                user.getGoal(),
                user.getLevel(),
                user.getPoint());
    }
}
