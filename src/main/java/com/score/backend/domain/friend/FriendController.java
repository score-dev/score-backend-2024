package com.score.backend.domain.friend;

import com.score.backend.domain.friend.block.BlockService;
import com.score.backend.domain.user.User;
import com.score.backend.domain.user.UserService;
import com.score.backend.dtos.FriendsSearchResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Friend", description = "내 친구 관리를 위한 API입니다.")
@RestController
@RequiredArgsConstructor
public class FriendController {
    private final UserService userService;
    private final FriendService friendService;
    private final BlockService blockService;

    @Operation(summary = "친구 추가", description = "새로운 친구를 추가합니다.")
    @RequestMapping(value = "/score/friends/new/{id1}/{id2}", method = RequestMethod.POST)
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "친구 추가 완료"),
                    @ApiResponse(responseCode = "404", description = "User Not Found")})
    public ResponseEntity<String> addNewFriend(@Parameter(required = true, description = "친구 신청 보낸 유저의 고유 id 값")@PathVariable("id1") Long id1,
                                                   @Parameter(required = true, description = "친구 신청 받은 유저의 고유 id 값") @PathVariable("id2") Long id2) throws BadRequestException {
        friendService.saveNewFriend(userService.findUserById(id1), userService.findUserById(id2));
        return ResponseEntity.ok("친구 추가가 완료되었습니다.");
    }

    @Operation(summary = "친구 삭제", description = "친구를 삭제합니다.")
    @DeleteMapping(value = "/score/friends/delete/{id1}/{id2}")
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "친구 삭제 완료"),
                    @ApiResponse(responseCode = "404", description = "User Not Found")})
    public ResponseEntity<String> removeFriend(@Parameter(required = true, description = "친구 삭제 요청을 한 유저의 고유 id 값") @PathVariable("id1") Long id1,
                                               @Parameter(required = true, description = "id1이 삭제하고자 하는 친구의 고유 id 값") @PathVariable("id2")  Long id2) {
        friendService.deleteFriend(id1, id2);
        return ResponseEntity.ok("친구 삭제가 완료되었습니다.");
    }

    @Operation(summary = "친구 차단", description = "친구룰 차단합니다.")
    @RequestMapping(value = "/score/friends/block/{id1}/{id2}", method = RequestMethod.POST)
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "친구 차단 완료"),
                    @ApiResponse(responseCode = "404", description = "User Not Found")})
    public ResponseEntity<String> blockFriend(@Parameter(required = true, description = "친구 차단 요청을 한 유저의 고유 id 값")@PathVariable Long id1,
                                                   @Parameter(required = true, description = "id1이 차단하고자 하는 친구의 고유 id 값") @PathVariable Long id2) throws BadRequestException {
        blockService.blockFriend(userService.findUserById(id1), userService.findUserById(id2));
        return ResponseEntity.ok("친구 차단이 완료되었습니다.");
    }

    @Operation(summary = "친구 목록 조회", description = "친구 목록을 조회합니다.")
    @RequestMapping(value = "/score/friends/list", method = RequestMethod.GET)
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "친구 목록 조회 완료"),
                    @ApiResponse(responseCode = "404", description = "User Not Found")})
    public ResponseEntity<Page<FriendsSearchResponse>> getAllFriends(
            @RequestParam("id") @Parameter(required = true, description = "친구 목록을 요청할 유저의 고유 번호") Long id,
            @RequestParam("page") @Parameter(required = true, description = "출력할 친구 리스트의 페이지 번호") int page) {
        return ResponseEntity.ok(friendService.getAllFriends(page, id));
    }

    @Operation(summary = "차단한 친구 목록 조회", description = "차단한 친구 목록을 조회합니다.")
    @RequestMapping(value = "/score/friends/blocked/list", method = RequestMethod.GET)
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "차단 친구 목록 조회 완료"),
                    @ApiResponse(responseCode = "404", description = "User Not Found")})
    public ResponseEntity<List<FriendsSearchResponse>> getBlockedFriends(@RequestParam("id") @Parameter(required = true, description = "차단 친구 목록을 요청한 유저의 고유 번호") Long id) {
        return ResponseEntity.ok(blockService.findBlockedFriends(userService.findUserById(id)));
    }

    @Operation(summary = "차단한 친구 차단 해제", description = "차단했던 친구에 대한 차단을 해제합니다.")
    @RequestMapping(value = "/score/friends/blocked/list", method = RequestMethod.PUT)
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "차단 해제 완료"),
                    @ApiResponse(responseCode = "404", description = "User Not Found")})
    public ResponseEntity<String> unblockBlockedUser(
            @RequestParam("id1") @Parameter(required = true, description = "친구 차단 해제를 요청한 유저의 고유 번호") Long id1,
            @RequestParam("id2") @Parameter(required = true, description = "차단에서 해제될 유저의 고유 번호") Long id2) {
        blockService.unblockFriend(userService.findUserById(id1), userService.findUserById(id2));
        return ResponseEntity.ok("차단 해제가 완료되었습니다.");
    }
}
