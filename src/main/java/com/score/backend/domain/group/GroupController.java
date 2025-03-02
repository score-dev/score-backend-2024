package com.score.backend.domain.group;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.score.backend.dtos.*;
import com.score.backend.domain.exercise.ExerciseService;
import com.score.backend.exceptions.ExceptionType;
import com.score.backend.exceptions.ScoreCustomException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Slf4j
@Tag(name = "Group", description = "그룹 정보 관리를 위한 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/score/groups")
public class GroupController {

    private final GroupService groupService;
    private final ExerciseService exerciseService;
    private final BatonService batonService;

    @Operation(summary = "그룹 생성", description = "새로운 그룹을 생성하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "그룹 생성이 완료. 생성된 그룹의 고유 id 값 응답."),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @PostMapping("/create")
    public ResponseEntity<Long> createGroup(
            @Parameter(description = "생성될 그룹 정보 전달을 위한 DTO", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)) @RequestPart(value = "groupCreateDto") GroupCreateDto groupCreateDto,
            @Parameter(description = "그룹 프로필 사진", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)) @RequestPart(value = "file") MultipartFile multipartFile) throws IOException {
        GroupEntity gr = groupService.createGroup(groupCreateDto, multipartFile);
        return ResponseEntity.ok(gr.getGroupId());
    }

    @Operation(summary = "내(모든) 그룹 목록 조회", description = "내(모든) 그룹의 목록을 조회하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모든 그룹 조회가 완료되었습니다.")
    })
    @GetMapping("/all")
    public ResponseEntity<List<GroupDto>> getAllGroups(@RequestParam("id") @Parameter(required = true, description = "그룹 목록을 요청한 유저의 고유 번호") Long userId) {
        List<GroupEntity> groups = groupService.findAllGroupsByUserId(userId);
        List<GroupDto> groupDtos = new ArrayList<>();
        for (GroupEntity group : groups) {
            groupDtos.add(GroupDto.fromEntity(group));
        }
        return ResponseEntity.ok(groupDtos);
    }

    // 현재 존재하지 않는 기능.
    @Operation(summary = "그룹 탈퇴", description = "사용자가 그룹을 탈퇴하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "그룹 탈퇴가 완료되었습니다."),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @PostMapping("/{groupId}/leave")
    public ResponseEntity<String> leaveGroup(@PathVariable Long groupId, @RequestParam Long userId) {
        groupService.leaveGroup(groupId, userId);
        return ResponseEntity.ok("그룹 탈퇴가 완료되었습니다.");
    }

//    @Operation(summary = "그룹 정보 수정", description = "그룹 정보를 수정하는 API입니다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "그룹 정보 수정이 완료되었습니다."),
//            @ApiResponse(responseCode = "400", description = "Bad Request")
//    })
//    @PutMapping("/{groupId}/update")
//    public ResponseEntity<String> updateGroup(@PathVariable Long groupId,
//                                              @Valid @RequestBody GroupCreateDto groupCreateDto) {
//        try {
//            groupService.updateGroup(groupId, groupCreateDto);
//            return ResponseEntity.ok("그룹 정보 update 완료!");
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }

    // 현재 존재하지 않는 기능
    @Operation(summary = "그룹 멤버 강퇴", description = "방장이 그룹에서 멤버를 강퇴하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "멤버 강퇴가 완료되었습니다."),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @DeleteMapping("/{groupId}/removeMember")
    public ResponseEntity<String> removeMemberFromGroup(@PathVariable Long groupId, @RequestParam Long userId) {
        groupService.removeMember(groupId, userId);
        return ResponseEntity.ok("멤버 강퇴가 완료되었습니다.");
    }

    @Operation(summary = "비공개 그룹 비밀번호 일치 여부 확인", description = "비공개 그룹에 가입하고자 할 때 유저가 입력한 비밀번호가 일치하는지 여부를 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "true 응답 시 비밀번호 일치, false 응답 시 비밀번호 불일치"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @GetMapping("/join/verify")
    public ResponseEntity<Boolean> verifyGroupPassword(@RequestParam("input") @Parameter(required = true, description = "유저가 입력한 비공개 그룹의 비밀번호") String input,
                                                       @RequestParam("groupId") @Parameter(required = true, description = "유저가 가입하려고 하는 그룹의 id 값") Long groupId) {
        return ResponseEntity.ok(groupService.verifyGroupPassword(input, groupId));
    }

    @Operation(summary = "그룹 가입 신청", description = "그룹 가입 신청 요청 발생 시 방장에게 알림을 전송합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "그룹 가입 신청 완료"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "409", description = "정원이 가득 차 새로운 유저가 가입할 수 없는 그룹입니다.")
    })
    @GetMapping("/join/request")
    public ResponseEntity<String> sendGroupJoinRequest(@RequestParam("groupId") Long groupId, @RequestParam("userId") Long userId) throws FirebaseMessagingException {
        if (groupService.checkEmptySpaceExistence(groupId)) {
            groupService.sendGroupJoinRequestNotification(groupId, userId);
            return ResponseEntity.ok("방장에게 그룹 가입 신청이 완료되었습니다.");
        } else {
            throw new ScoreCustomException(ExceptionType.FULL_GROUP);
        }
    }

    @Operation(summary = "그룹 가입", description = "그룹장이 그룹 가입 신청 승인 시 해당 유저를 그룹에 가입시킵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "그룹 가입이 완료되었습니다."),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "409", description = "정원이 가득 차 새로운 유저가 가입할 수 없는 그룹입니다.")
    })
    @PutMapping("/join/accepted")
    public ResponseEntity<String> joinGroup(@RequestParam("groupId") Long groupId, @Parameter(required = true, description = "가입 처리할 유저의 id 값") @RequestParam("userId") Long userId) throws FirebaseMessagingException, BadRequestException {
        if (groupService.checkEmptySpaceExistence(groupId)) {
            groupService.addNewMember(groupId, userId);
            return ResponseEntity.ok("그룹 가입이 완료되었습니다.");
        }
        return ResponseEntity.status(409).body("정원이 가득 차 새로운 유저가 가입할 수 없는 그룹입니다.");
    }

    @Operation(summary = "그룹 정보 조회", description = "그룹 정보를 조회합니다.")
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "가입된 그룹인지 여부에 따라 그룹 정보를 응답합니다."),
                    @ApiResponse(responseCode = "409", description = "가입되어 있지 않은 비공개 그룹에 대한 그룹 정보 조회 요청입니다."),
                    @ApiResponse(responseCode = "404", description = "User Not Found"),
                    @ApiResponse(responseCode = "400", description = "Bad Request")}
    )
    @RequestMapping(value = "/info", method = GET)
    public ResponseEntity<GroupInfoResponse> getGroupInfo(@RequestParam("userId") Long userId, @RequestParam("groupId") Long groupId) {
        if (groupService.isMemberOfGroup(groupId, userId)) {
            GroupInfoResponse response = groupService.getGroupInfoForMember(groupId);
            return ResponseEntity.ok(response);
        } else {
            if (groupService.findById(groupId).isPrivate()) {
                return ResponseEntity.status(409).body(new GroupInfoResponse());
            } else {
                GroupInfoResponse response = groupService.getGroupInfoForNonMember(groupId);
                return ResponseEntity.ok(response);
            }
        }
    }

    @Operation(summary = "그룹 피드 목록 조회", description = "그룹원이 업로드한 전체 피드 목록을 페이지 단위로 제공합니다.")
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "유저가 가입되어 있는 그룹에 대한 피드 목록 조회 요청이라면 피드에 대한 모든 정보를, 가입되어 있지 않은 그룹에 대한 요청이라면 피드 이미지만을 응답합니다."),
                    @ApiResponse(responseCode = "400", description = "Bad Request")}
    )
    @RequestMapping(value = "/exercise/list", method = GET)
    public ResponseEntity<Page<FeedInfoResponse>> getAllGroupsFeeds(
            @RequestParam("userId") @Parameter(required = true, description = "피드 목록을 요청한 유저의 고유 번호") Long userId,
            @RequestParam("groupId") @Parameter(required = true, description = "피드 목록을 요청할 그룹의 고유 번호") Long groupId,
            @RequestParam("page") @Parameter(required = true, description = "출력할 피드 리스트의 페이지 번호") int page) {
        if (groupService.isMemberOfGroup(groupId, userId)) {
            return ResponseEntity.ok(exerciseService.getGroupsAllExercises(page, groupId));
        } else {
            return ResponseEntity.ok(exerciseService.getGroupsAllExercisePics(page, groupId));
        }
    }

    @Operation(summary = "그룹 내 메이트 전체 조회", description = "그룹에 가입해 있는 전체 메이트들의 목록을 조회합니다.")
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "메이트 목록 조회가 완료되었습니다."),
                    @ApiResponse(responseCode = "404", description = "Group Not Found")
            })
    @GetMapping(value = "/mates/list")
    public ResponseEntity<List<UserResponseDto>> getAllGroupMates (
            @RequestParam("groupId") @Parameter(required = true, description = "조회할 그룹의 id") Long groupId) {
        List<UserResponseDto> dtoList = groupService.findAllUsers(groupId);
        return ResponseEntity.ok(dtoList);
    }

    @Operation(summary = "오늘 운동을 쉰 메이트의 목록 조회", description = "그룹 내에서 오늘 운동을 쉰 메이트의 목록을 조회합니다.")
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "오늘 운동을 쉰 메이트 조회가 완료되었습니다."),
                    @ApiResponse(responseCode = "404", description = "User Not Found")
            })
    @GetMapping(value = "/mates/nonExercised")
    public ResponseEntity<List<BatonStatusDto>> getNotExercisedMatesList(
            @RequestParam("groupId") @Parameter(required = true, description = "조회할 그룹의 id") Long groupId,
            @RequestParam("userId") @Parameter(required = true, description = "조회를 요청한 유저의 id. 바통 찌르기를 이미 했는지 여부 조회 위해 필요.") Long userId) {
        List<BatonStatusDto> dtoList = batonService.getBatonStatuses(userId, groupId);
        return ResponseEntity.ok(dtoList);
    }

    @Operation(summary = "바통 찌르기", description = "오늘 운동하지 않은 유저에게 바통 찌르기 알림을 보냅니다.")
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "Response Body가 True이면 바통 찌르기 완료. False이면 오늘 이미 바통을 찌른 유저."),
                    @ApiResponse(responseCode = "400", description = "FirebaseMessagingException"),
                    @ApiResponse(responseCode = "404", description = "User Not Found")
            })
    @PostMapping(value = "/mates/baton")
    public ResponseEntity<Boolean> turnOverBaton(
            @RequestParam("senderId") @Parameter(required = true, description = "바통을 찌른 유저의 id") Long senderId,
            @RequestParam("receiverId") @Parameter(required = true, description = "바통을 찔린 유저의 id") Long receiverId) throws FirebaseMessagingException {
        Boolean wasTurned = batonService.turnOverBaton(senderId, receiverId);
        return ResponseEntity.ok(wasTurned);
    }
}
