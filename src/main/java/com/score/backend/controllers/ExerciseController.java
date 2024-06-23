package com.score.backend.controllers;

import com.score.backend.models.dtos.WalkingDto;
import com.score.backend.models.exercise.Exercise;
import com.score.backend.services.ExerciseService;
import com.score.backend.services.LevelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@Tag(name = "Exercise", description = "운동 기록 및 피드 관리를 위한 API입니다.")
@RestController
@RequiredArgsConstructor
public class ExerciseController {
    private final ExerciseService exerciseService;
    private final LevelService levelService;


    @Operation(summary = "피드 저장", description = "운동이 끝난 후 운동 기록을 업데이트하고, 피드를 업로드합니다.")
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "피드 업로드 후 이전 페이지로 리다이렉트", headers = {@Header(name = "new URI", schema = @Schema(type = "string"))}),
                    @ApiResponse(responseCode = "400", description = "Bad Request")}
    )
    @RequestMapping(value = "/score/exercise/walking/save", method = POST)
    public ResponseEntity<Object> uploadWalkingFeed(@Parameter(description = "운동 결과 전달을 위한 DTO", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)) @RequestPart(value = "walkingDto") WalkingDto walkingDto,
                                                    @Parameter(description = "피드에 업로드할 이미지", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)) @RequestPart(value = "file") MultipartFile multipartFile,
                                                    HttpServletResponse response) throws IOException {
        // 피드 저장
        exerciseService.saveFeed(walkingDto, multipartFile);
        // 유저의 연속 운동 일수 증가
        boolean isIncreased = exerciseService.increaseConsecutiveDate(walkingDto.getAgentId());
        // 연속 운동 일수 증가에 따른 포인트 증가
        if (isIncreased) {
            levelService.increasePointsByConsecutiveDate(walkingDto.getAgentId());
        }
        // 누적 운동 거리에 따른 포인트 증가
        levelService.increasePointsByWalkingDistance(walkingDto.getAgentId(), walkingDto.getDistance());
        // 누적 운동 거리 업데이트
        exerciseService.cumulateExerciseDistance(walkingDto.getAgentId(), walkingDto.getDistance());
        // 누적 운동 시간 업데이트
        exerciseService.cumulateExerciseDuration(walkingDto.getAgentId(), walkingDto.getStartedAt(), walkingDto.getCompletedAt());
        // 피드 업로드에 따른 포인트 증가
        levelService.increasePointsForTodaysFirstExercise(walkingDto.getAgentId());
        response.getOutputStream().close();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create("http://localhost:8080/score/main"));
        return new ResponseEntity<>(response, httpHeaders, HttpStatus.MOVED_PERMANENTLY);
    }

    @Operation(summary = "피드 삭제", description = "피드를 삭제합니다.")
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "피드 삭제 후 이전 페이지로 리다이렉트", headers = {@Header(name = "new URI", schema = @Schema(type = "string"))}),
                    @ApiResponse(responseCode = "400", description = "Bad Request")}
    )
    @RequestMapping(value = "/score/exercise/walking/delete", method = DELETE)
    public ResponseEntity<Object> deleteFeed(@RequestParam("id") @Parameter(required = true, description = "피드 고유 번호") Long id, HttpServletResponse response) {
        Exercise feed = exerciseService.findFeedByExerciseId(id).orElseThrow(
                () -> new RuntimeException("Exercise not found")
        );
        exerciseService.deleteFeed(feed);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create("http://localhost:8080/score/main"));
        return new ResponseEntity<>(response, httpHeaders, HttpStatus.MOVED_PERMANENTLY);
    }

    @Operation(summary = "유저의 피드 목록 조회", description = "유저의 전체 피드 목록을 페이지 단위로 제공합니다.")
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "피드 페이지가 JSON 형태로 전달됩니다."),
                    @ApiResponse(responseCode = "400", description = "Bad Request")}
    )
    @RequestMapping(value = "/score/exercise/list", method = GET)
    public Page<Exercise> getAllFeeds(@RequestParam("id") @Parameter(required = true, description = "피드 목록을 요청할 유저의 고유 번호") Long id,
                                      @RequestParam("page") @Parameter(required = true, description = "출력할 피드 리스트의 페이지 번호") int page) {
        return exerciseService.getUsersAllExercises(page, id);
    }
}
