package com.score.backend.domain.exercise;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.score.backend.domain.exercise.emotion.EmotionService;
import com.score.backend.domain.friend.Friend;
import com.score.backend.domain.user.User;
import com.score.backend.domain.user.UserService;
import com.score.backend.dtos.FeedInfoResponse;
import com.score.backend.dtos.FriendsSearchResponse;
import com.score.backend.dtos.WalkingDto;
import com.score.backend.domain.friend.FriendService;
import com.score.backend.domain.group.GroupService;
import com.score.backend.domain.user.level.LevelService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@Tag(name = "Exercise", description = "운동 기록 및 피드 관리를 위한 API입니다.")
@RestController
@RequiredArgsConstructor
public class ExerciseController {
    private final UserService userService;
    private final ExerciseService exerciseService;
    private final LevelService levelService;
    private final FriendService friendService;
    private final GroupService groupService;
    private final EmotionService emotionService;

    @Operation(summary = "함께 운동한 친구 검색", description = "함께 운동한 친구를 선택하기 위해 닉네임으로 검색합니다.")
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "검색 완료."),
                    @ApiResponse(responseCode = "404", description = "검색 결과가 존재하지 않습니다.")}
    )
    @RequestMapping(value = "/score/exercise/friends", method = GET)
    public ResponseEntity<List<FriendsSearchResponse>> searchFriendsByNickname(
            @Parameter(description = "운동을 기록하고자 하는(함께 운동할 친구를 선택하고자 하는) 유저의 고유 id 값", required = true) @RequestParam Long id,
            @Parameter(description = "유저가 필드에 입력한 내친구의 닉네임", required = true) @RequestParam String nickname) {

        List<Friend> searched = friendService.getFriendsByNicknameContaining(id, nickname);
        if (searched.isEmpty()) {
            throw new NoSuchElementException("검색 결과가 존재하지 않습니다.");
        }
        List<FriendsSearchResponse> responses = new ArrayList<>();
        for (Friend friend : searched) {
            responses.add(new FriendsSearchResponse(friend.getFriend().getId(), friend.getFriend().getNickname(), friend.getFriend().getProfileImg()));
        }
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @Operation(summary = "피드 저장", description = "운동이 끝난 후 운동 기록을 업데이트하고, 피드를 업로드합니다.")
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "피드 업로드 완료"),
                    @ApiResponse(responseCode = "404", description = "User Not Found")}
    )
    @RequestMapping(value = "/score/exercise/walking/save", method = POST)
    public ResponseEntity<String> uploadWalkingFeed(@Parameter(description = "운동 결과 전달을 위한 DTO", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = WalkingDto.class))) @RequestPart(value = "walkingDto") WalkingDto walkingDto,
                                                    @Parameter(description = "피드에 업로드할 이미지", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)) @RequestPart(value = "file") MultipartFile multipartFile) throws IOException, FirebaseMessagingException {

        User agent = userService.findUserById(walkingDto.getAgentId());
        List<User> others = walkingDto.getOthersId().stream().map(userService::findUserById).toList();
        ///// 3분 이상 운동한 경우 /////
        if (exerciseService.isValidateExercise(walkingDto.getStartedAt(), walkingDto.getCompletedAt())) {
            // 피드 업로드에 따른 포인트 증가
            levelService.increasePointsForTodaysFirstExercise(agent);
            // 마지막 운동 날짜 업데이트
            exerciseService.updateLastExerciseDateTime(agent, walkingDto.getCompletedAt());

            /////// 오늘 처음으로 3분 이상 운동한 경우 ///////
            if (exerciseService.isTodaysFirstValidateExercise(agent)) {
                // 유저의 연속 운동 일수 증가
                exerciseService.increaseConsecutiveDate(agent);
                // 연속 운동 일수 증가에 따른 포인트 증가
                levelService.increasePointsByConsecutiveDate(agent);
                // 유저의 금주 운동 현황 업데이트(유저의 금주 누적 운동 시간 업데이트+이번주 운동한 날짜 수 증가)
                exerciseService.updateWeeklyExerciseStatus(agent, true, walkingDto.getStartedAt(), walkingDto.getCompletedAt());
            }
            /////// 오늘 3분 이상 운동한 기록이 이미 존재하는 경우 ///////
            else {
                // 유저의 금주 누적 운동 시간 업데이트하지만 이번주 운동한 날짜 수는 더 이상 증가하지 않음
                exerciseService.updateWeeklyExerciseStatus(agent, false, walkingDto.getStartedAt(), walkingDto.getCompletedAt());
            }
        }

        ///// 3분 이상 운동 여부와 상관 없이 실행 /////
        // 누적 운동 거리에 따른 포인트 증가
        levelService.increasePointsByWalkingDistance(agent, walkingDto.getDistance());
        // 누적 운동 거리 업데이트
        exerciseService.cumulateExerciseDistance(agent, walkingDto.getDistance());
        // 개인 누적 운동 시간 업데이트
        exerciseService.cumulateExerciseDuration(agent, walkingDto.getStartedAt(), walkingDto.getCompletedAt());
        // 유저가 속한 그룹의 누적 운동 시간 업데이트
        groupService.increaseCumulativeTime(agent, walkingDto.getStartedAt(), walkingDto.getCompletedAt());
        // 피드 저장
        exerciseService.saveFeed(agent, others, walkingDto, multipartFile);
        return ResponseEntity.ok("피드 등록이 완료되었습니다.");
    }

    @Operation(summary = "피드 조회", description = "요청한 피드에 대한 세부 정보를 조회하여 응답합니다.")
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "요청한 피드의 세부 정보 응답", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = WalkingDto.class))),
                    @ApiResponse(responseCode = "404", description = "Feed Not Found")}
    )
    @RequestMapping(value = "/score/exercise/read", method = GET)
    public ResponseEntity<FeedInfoResponse> readFeed(@RequestParam("feedId") @Parameter(required = true, description = "조회하고자 하는 피드의 고유 번호") Long feedId) {
        Exercise feed = exerciseService.findFeedByExerciseId(feedId);
        return ResponseEntity.ok(new FeedInfoResponse(feed));
    }

    @Operation(summary = "피드 삭제", description = "피드를 삭제합니다.")
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "피드 삭제 완료"),
                    @ApiResponse(responseCode = "404", description = "Feed Not Found")}
    )
    @RequestMapping(value = "/score/exercise/delete", method = DELETE)
    public ResponseEntity<String> deleteFeed(@RequestParam("id") @Parameter(required = true, description = "피드 고유 번호") Long feedId) {
        Exercise feed = exerciseService.findFeedByExerciseId(feedId);
        exerciseService.deleteFeed(feed);
        emotionService.deleteAllEmotions(feed);
        return ResponseEntity.ok("피드 삭제가 완료되었습니다.");
    }

    @Operation(summary = "유저의 피드 목록 조회", description = "유저의 전체 피드 목록을 페이지 단위로 제공합니다.")
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "피드 페이지가 JSON 형태로 전달됩니다."),
                    @ApiResponse(responseCode = "404", description = "User Not Found"),
                    @ApiResponse(responseCode = "409", description = "차단한 유저에 대한 피드 목록 조회 요청입니다.")
            })
    @RequestMapping(value = "/score/exercise/list", method = GET)
    public ResponseEntity<Page<FeedInfoResponse>> getAllUsersFeeds(@RequestParam("id1") @Parameter(required = true, description = "피드 목록을 요청한 유저의 고유 번호") Long id1,
                                                                   @RequestParam("id2") @Parameter(required = true, description = "id1 유저가 피드를 조회하고자 하는 유저의 고유 번호") Long id2,
                                                                   @RequestParam("page") @Parameter(required = true, description = "출력할 피드 리스트의 페이지 번호") int page) {
        return ResponseEntity.ok(exerciseService.getUsersAllExercises(page, userService.findUserById(id1), userService.findUserById(id2)));
    }
}
