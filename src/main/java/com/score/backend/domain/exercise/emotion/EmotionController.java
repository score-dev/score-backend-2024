package com.score.backend.domain.exercise.emotion;

import com.score.backend.dtos.EmotionStatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Emotion", description = "피드 감정 표현 관리를 위한 API입니다.")
@RestController
@RequiredArgsConstructor
public class EmotionController {
    private final EmotionService emotionService;
    private final EmotionFacade emotionFacade;

    @Operation(summary = "감정 표현 추가 혹은 제거", description = "피드에 요청한 감정 표현을 한 적이 있는 유저라면 그 감정 표현을 삭제하고, 요청한 감정 표현을 한 적이 없는 유저라면 그 감정 표현을 추가합니다.")
    @RequestMapping(value = "/score/exercise/emotion", method = RequestMethod.POST)
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "감정 표현 추가 혹은 삭제 완료"),
                    @ApiResponse(responseCode = "404", description = "Agent Or Feed Not Found"),
                    @ApiResponse(responseCode = "400", description = "존재하지 않는 감정 표현입니다.")})
    public ResponseEntity<String> addOrDeleteEmotion(
            @Parameter(required = true, description = "감정 표현을 누르는 유저의 고유 id 값") @RequestParam("agentId") Long agentId,
            @Parameter(required = true, description = "감정 표현을 누른 피드 게시물의 고유 id 값") @RequestParam("feedId") Long feedId,
            @Parameter(required = true, description = "좋아요(like), 최고예요(best), 응원해요(support), 축하해요(congrat), 일등이에요(first) 중 어떤 감정 표현인지", example = "like")
            @RequestParam("type") EmotionType type) {
        if (emotionFacade.safeAddOrDeleteEmotion(agentId, feedId, type)) {
            return ResponseEntity.ok("감정 표현 추가가 완료되었습니다.");
        }
        return ResponseEntity.ok("감정 표현 삭제가 완료되었습니다.");
    }

    @Operation(summary = "피드에 등록된 모든 감정 표현 목록 조회", description = "감정 표현 타입에 상관 없이 해당 피드에 추가되어 있는 모든 감정 표현의 리스트를 응답합니다.")
    @RequestMapping(value = "/score/exercise/emotion/list/all", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "감정 표현 조회 완료"),
            @ApiResponse(responseCode = "404", description = "Agent Or Feed Not Found")})
    public ResponseEntity<List<EmotionStatusResponse>> showAllEmotions(
            @Parameter(required = true, description = "감정 표현 목록을 확인할 피드 게시물의 고유 id 값") @RequestParam("feedId") Long feedId) {
        return ResponseEntity.ok(emotionService.makeEmotionListDto(feedId));
    }

    @Operation(summary = "특정 타입의 감정 표현 목록 조회", description = "해당 피드에 추가되어 있는 특정 타입의 감정 표현의 리스트를 응답합니다.")
    @RequestMapping(value = "/score/exercise/emotion/list/types", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "감정 표현 조회 완료"),
            @ApiResponse(responseCode = "404", description = "Agent Or Feed Not Found")})
    public ResponseEntity<List<Emotion>> showCertainTypeOfEmotions(
            @Parameter(required = true, description = "감정 표현 목록을 확인할 피드 게시물의 고유 id 값") @RequestParam("feedId") Long feedId,
            @Parameter(required = true, description = "조회할 감정 표현의 종류") @RequestParam("emotionType") EmotionType emotionType) {
        List<Emotion> emotions = emotionService.findAllEmotionTypes(feedId, emotionType);
        return ResponseEntity.ok(emotions);
    }
}
