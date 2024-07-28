package com.score.backend.controllers;

import com.score.backend.models.User;
import com.score.backend.services.FriendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Friend", description = "내 친구 관리를 위한 API입니다.")
@RestController
@RequiredArgsConstructor
public class FriendController {
    private final FriendService friendService;

    @Operation(summary = "친구 추가", description = "새로운 친구를 추가합니다.")
    @RequestMapping(value = "/score/friends/new/{id1}/{id2}", method = RequestMethod.POST)
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

    @Operation(summary = "친구 삭제", description = "친구를 삭제합니다.")
    @RequestMapping(value = "/score/friends/delete/{id1}/{id2}", method = RequestMethod.DELETE)
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "친구 삭제 완료"),
                    @ApiResponse(responseCode = "404", description = "User Not Found")})
    public ResponseEntity<HttpStatus> removeFriend(@Parameter(required = true, description = "친구 삭제 요청을 한 유저의 고유 id 값")@PathVariable Long id1,
                                                   @Parameter(required = true, description = "id1이 삭제하고자 하는 친구의 고유 id 값") @PathVariable Long id2) {
        try {
            friendService.deleteFriend(id1, id2);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "친구 차단", description = "친구룰 차단합니다.")
    @RequestMapping(value = "/score/friends/block/{id1}/{id2}", method = RequestMethod.POST)
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "친구 차단 완료"),
                    @ApiResponse(responseCode = "404", description = "User Not Found")})
    public ResponseEntity<HttpStatus> blockFriend(@Parameter(required = true, description = "친구 차단 요청을 한 유저의 고유 id 값")@PathVariable Long id1,
                                                   @Parameter(required = true, description = "id1이 차단하고자 하는 친구의 고유 id 값") @PathVariable Long id2) {
        try {
            friendService.blockFriend(id1, id2);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "친구 목록 조회", description = "친구 목록을 조회합니다.")
    @RequestMapping(value = "/score/friends/list/{id1}", method = RequestMethod.GET)
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "친구 목록 조회 완료"),
                    @ApiResponse(responseCode = "404", description = "User Not Found")})
    public ResponseEntity<Page<User>> getAllFriends(
            @RequestParam("id") @Parameter(required = true, description = "친구 목록을 요청할 유저의 고유 번호") Long id,
            @RequestParam("page") @Parameter(required = true, description = "출력할 친구 리스트의 페이지 번호") int page) {
        return ResponseEntity.ok(friendService.getAllFriends(page, id));
    }



}
