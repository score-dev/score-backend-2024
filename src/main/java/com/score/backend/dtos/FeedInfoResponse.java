package com.score.backend.dtos;

import com.score.backend.domain.exercise.Exercise;
import com.score.backend.domain.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "피드 정보 응답을 위한 DTO")
public class FeedInfoResponse {
    @Schema(description = "피드를 식별할 수 있는 고유 id")
    private Long feedId;
    @Schema(description = "피드를 등록한 유저의 닉네임")
    private String uploaderNickname;
    @Schema(description = "피드를 등록한 유저의 프로필 사진 url")
    private String uploaderProfileImgUrl;
    @Schema(description = "피드에 등록된 사진 url")
    private String feedImg;
    @Schema(description = "운동 시작 시간")
    private LocalDateTime startedAt;
    @Schema(description = "운동을 끝낸 시간")
    private LocalDateTime completedAt;
    @Schema(description = "운동한 위치")
    private String location;
    @Schema(description = "날씨")
    private String weather;
    @Schema(description = "기온")
    private int temperature;
    @Schema(description = "미세먼지 농도")
    private String fineDust;
    @Schema(description = "운동 소감")
    private String feeling;
    @Schema(description = "함께 운동한 유저들의 닉네임")
    private List<String> taggedNicknames;
    @Schema(description = "함께 운동한 유저들의 프로필 사진 url")
    private List<String> taggedProfileImgUrls;

    public FeedInfoResponse(Exercise exercise) {
        this.feedId = exercise.getId();
        this.uploaderNickname = exercise.getAgent().getNickname();
        this.uploaderProfileImgUrl = exercise.getAgent().getProfileImg();
        this.feedImg = exercise.getExercisePic();
        this.startedAt = exercise.getStartedAt();
        this.completedAt = exercise.getCompletedAt();
        this.location = exercise.getLocation();
        this.weather = exercise.getWeather();
        this.temperature = exercise.getTemperature();
        this.fineDust = exercise.getFineDust();
        this.feeling = exercise.getFeeling();
        this.taggedNicknames = exercise.getTaggedUsers().stream().map(User::getNickname)
                .collect(Collectors.toList());
        this.taggedProfileImgUrls = exercise.getTaggedUsers().stream().map(User::getProfileImg)
                .collect(Collectors.toList());
    }

    public Page<FeedInfoResponse> toDtoListForMates(Page<Exercise> exercises) {
        return exercises.map(m -> FeedInfoResponse.builder()
                .feedId(m.getId())
                .uploaderNickname(m.getAgent().getNickname())
                .uploaderProfileImgUrl(m.getAgent().getProfileImg())
                .feedImg(m.getExercisePic())
                .startedAt(m.getStartedAt())
                .completedAt(m.getCompletedAt())
                .location(m.getLocation())
                .weather(m.getWeather())
                .temperature(m.getTemperature())
                .fineDust(m.getFineDust())
                .feeling(m.getFeeling())
                .taggedNicknames(m.getTaggedUsers().stream().map(User::getNickname).toList())
                .taggedProfileImgUrls(m.getTaggedUsers().stream().map(User::getProfileImg).toList())
                .build());
    }

    public Page<FeedInfoResponse> toDtoListForNonMates(Page<Exercise> exercises) {
        return exercises.map(m -> FeedInfoResponse.builder().feedImg(m.getExercisePic()).build());
    }
}
