package com.score.backend.controllers;

import com.score.backend.services.FriendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Friend", description = "내 친구 관리를 위한 API입니다.")
@RestController
@RequiredArgsConstructor
public class FriendController {
    private final FriendService friendService;

    @Operation(summary = "친구 추가", description = "새로운 친구를 추가합니다.")
    @RequestMapping(value = "/score/friends/new/{id1}/{id2}", method = RequestMethod.GET)
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "친구 추가 완료"),
                    @ApiResponse(responseCode = "404", description = "User Not Found")})
    public ResponseEntity<HttpStatus> addNewFriend(@Parameter(required = true, description = "친구 신청 보낸 유저의 고유 id 값")@PathVariable Long id1,
                                                   @Parameter(required = true, description = "친구 신청 받은 유저의 고유 id 값") @PathVariable Long id2) {
        try {
            friendService.saveNewFriend(id1, id2);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
