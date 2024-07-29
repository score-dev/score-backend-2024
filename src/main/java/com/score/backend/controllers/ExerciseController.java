package com.score.backend.controllers;

import com.score.backend.models.dtos.WalkingDto;
import com.score.backend.models.exercise.Exercise;
import com.score.backend.services.ExerciseService;
import com.score.backend.services.LevelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@Tag(name = "Exercise", description = "운동 기록 및 피드 관리를 위한 API입니다.")
@RestController
@RequiredArgsConstructor
public class ExerciseController {
    private final ExerciseService exerciseService;
    private final LevelService levelService;


    @Operation(summary = "피드 저장", description = "운동이 끝난 후 운동 기록을 업데이트하고, 피드를 업로드합니다.")
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "피드 업로드 완료"),
                    @ApiResponse(responseCode = "404", description = "User Not Found")}
    )
    @RequestMapping(value = "/score/exercise/walking/save", method = POST)
    public ResponseEntity<HttpStatus> uploadWalkingFeed(@Parameter(description = "운동 결과 전달을 위한 DTO", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = WalkingDto.class))) @RequestPart(value = "walkingDto") WalkingDto walkingDto,
                                                    @Parameter(description = "피드에 업로드할 이미지", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)) @RequestPart(value = "file") MultipartFile multipartFile) {
        try {
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
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "피드 조회", description = "요청한 피드에 대한 세부 정보를 조회하여 응답합니다.")
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "요청한 피드의 세부 정보 응답", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = WalkingDto.class))),
                    @ApiResponse(responseCode = "404", description = "Feed Not Found")}
    )
    @RequestMapping(value = "/score/exercise/read", method = GET)
    public ResponseEntity<Exercise> readFeed(@RequestParam("feedId") @Parameter(required = true, description = "조회하고자 하는 피드의 고유 번호") Long feedId) {
        Optional<Exercise> feed = exerciseService.findFeedByExerciseId(feedId);
        return feed.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @Operation(summary = "피드 삭제", description = "피드를 삭제합니다.")
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "피드 삭제 완료"),
                    @ApiResponse(responseCode = "404", description = "Feed Not Found")}
    )
    @RequestMapping(value = "/score/exercise/delete", method = DELETE)
    public ResponseEntity<HttpStatusCode> deleteFeed(@RequestParam("id") @Parameter(required = true, description = "피드 고유 번호") Long id) {
        Optional<Exercise> feed = exerciseService.findFeedByExerciseId(id);
        if (feed.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        exerciseService.deleteFeed(feed.get());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "유저의 피드 목록 조회", description = "유저의 전체 피드 목록을 페이지 단위로 제공합니다.")
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "피드 페이지가 JSON 형태로 전달됩니다."),
                    @ApiResponse(responseCode = "404", description = "User Not Found"),
                    @ApiResponse(responseCode = "409", description = "차단한 유저에 대한 피드 목록 조회 요청입니다.")
            })
    @RequestMapping(value = "/score/exercise/list", method = GET)
    public ResponseEntity<Page<Exercise>> getAllUsersFeeds(@RequestParam("id1") @Parameter(required = true, description = "피드 목록을 요청한 유저의 고유 번호") Long id1,
                                                           @RequestParam("id2") @Parameter(required = true, description = "id1 유저가 피드를 조회하고자 하는 유저의 고유 번호") Long id2,
                                      @RequestParam("page") @Parameter(required = true, description = "출력할 피드 리스트의 페이지 번호") int page) {
        try {
            Page<Exercise> feeds = exerciseService.getUsersAllExercises(page, id1, id2);
            return ResponseEntity.ok(feeds);
        } catch (NoSuchElementException e1) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(404));
        } catch (RuntimeException e2) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(409));
        }

    }

    @Operation(summary = "그룹 피드 목록 조회", description = "그룹원이 업로드한 전체 피드 목록을 페이지 단위로 제공합니다.")
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "피드 페이지가 JSON 형태로 전달됩니다."),
                    @ApiResponse(responseCode = "400", description = "Bad Request")}
    )
    @RequestMapping(value = "/score/group/exercise/list", method = GET)
    public ResponseEntity<Page<Exercise>> getAllGroupsFeeds(@RequestParam("id") @Parameter(required = true, description = "피드 목록을 요청할 그룹의 고유 번호") Long id,
                                                      @RequestParam("page") @Parameter(required = true, description = "출력할 피드 리스트의 페이지 번호") int page) {
        return ResponseEntity.ok(exerciseService.getGroupsAllExercises(page, id));
    }


}