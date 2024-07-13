package com.score.backend.controllers;

import com.score.backend.models.enums.EmotionType;
import com.score.backend.services.EmotionService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Emotion", description = "피드 감정 표현 관리를 위한 API입니다.")
@RestController
@RequiredArgsConstructor
public class EmotionController {

    private final EmotionService emotionService;

    @RequestMapping(value = "/score/exercise/emotion", method = RequestMethod.POST)
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "피드 삭제 완료"),
                    @ApiResponse(responseCode = "404", description = "Agent Or Feed Not Found"),
                    @ApiResponse(responseCode = "400", description = "존재하지 않는 감정 표현입니다.")})
    public ResponseEntity<HttpStatusCode> addEmotionAtFeed(
            @Parameter(required = true, description = "감정 표현을 누르는 유저의 고유 id 값") @RequestParam("agentId") Long agentId,
            @Parameter(required = true, description = "감정 표현을 누른 피드 게시물의 고유 id 값") @RequestParam("feedId") Long feedId,
            @Parameter(required = true, description = "좋아요(like), 최고예요(best), 응원해요(support), 축하해요(congrat), 일등이에요(first) 중 어떤 감정 표현인지", example = "like")
            @RequestParam("type") EmotionType type) {
        try {
            emotionService.addEmotion(agentId, feedId, type);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
