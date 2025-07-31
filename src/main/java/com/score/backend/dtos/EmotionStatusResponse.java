package com.score.backend.dtos;

import com.score.backend.domain.exercise.emotion.Emotion;
import com.score.backend.domain.exercise.emotion.EmotionType;
import com.score.backend.domain.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Schema(description = "피드에 등록된 감정 표현의 목록 응답을 위한 DTO")
@Getter
@AllArgsConstructor
public class EmotionStatusResponse {
    @Schema(description = "감정 표현을 남긴 유저의 고유 id 값")
    private Long agentId;
    @Schema(description = "감정 표현을 남긴 유저의 프로필 이미지 url")
    private String agentProfileImgUrl;
    @Schema(description = "감정 표현을 남긴 유저의 닉네임")
    private String agentNickname;
    @Schema(description = "유저가 남긴 감정 표현의 종류")
    private EmotionType emotionType;

    public static EmotionStatusResponse of(User agent, Emotion emotion) {
        return new EmotionStatusResponse(agent.getId(), agent.getProfileImg(), agent.getNickname(), emotion.getEmotionType());
    }
}
