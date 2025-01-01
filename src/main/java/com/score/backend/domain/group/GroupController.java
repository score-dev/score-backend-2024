package com.score.backend.domain.group;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.score.backend.dtos.*;
import com.score.backend.domain.rank.group.GroupRanking;
import com.score.backend.domain.exercise.ExerciseService;
import com.score.backend.domain.rank.group.GroupRankService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

import static org.springframework.format.annotation.DateTimeFormat.ISO.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Slf4j
@Tag(name = "Group", description = "그룹 정보 관리를 위한 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupService groupService;
    private final ExerciseService exerciseService;
    private final GroupRankService groupRankingService;
    private final BatonService batonService;

    @Operation(summary = "그룹 생성", description = "새로운 그룹을 생성하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "그룹 생성이 완료되었습니다."),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @PostMapping("/create")
    public ResponseEntity<String> createGroup(@Valid @RequestBody GroupCreateDto groupCreateDto, @RequestParam Long adminId) {
        try {
            groupCreateDto.isValid();  // 추가 검증
            groupService.createGroup(groupCreateDto, adminId);
            return ResponseEntity.ok("그룹 생성이 완료되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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

    @Operation(summary = "그룹 탈퇴", description = "사용자가 그룹을 탈퇴하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "그룹 탈퇴가 완료되었습니다."),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @PostMapping("/{groupId}/leave")
    public ResponseEntity<String> leaveGroup(@PathVariable Long groupId, @RequestParam Long userId) {
        try {
            groupService.leaveGroup(groupId, userId);
            return ResponseEntity.ok("그룹 탈퇴가 완료되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "그룹 정보 수정", description = "그룹 정보를 수정하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "그룹 정보 수정이 완료되었습니다."),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @PutMapping("/{groupId}/update")
    public ResponseEntity<String> updateGroup(@PathVariable Long groupId,
                                              @Valid @RequestBody GroupCreateDto groupCreateDto,
                                              @RequestParam Long adminId) {
        try {
            groupService.updateGroup(groupId, groupCreateDto, adminId);
            return ResponseEntity.ok("그룹 정보 update 완료!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "그룹 멤버 강퇴", description = "방장이 그룹에서 멤버를 강퇴하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "멤버 강퇴가 완료되었습니다."),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @DeleteMapping("/{groupId}/removeMember")
    public ResponseEntity<String> removeMemberFromGroup(@PathVariable Long groupId, @RequestParam Long userId) {
        try {
            groupService.removeMember(groupId, userId);
            return ResponseEntity.ok("멤버 강퇴가 완료되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "그룹 가입", description = "그룹에 가입합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "그룹 가입이완료되었습니다."),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @PutMapping("/join")
    public ResponseEntity<String> joinGroup(@RequestParam("groupId") Long groupId, @RequestParam("userId") Long userId) {
        try {
            groupService.addNewMember(groupId, userId);
            return ResponseEntity.ok("그룹 가입이 완료되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "그룹 랭킹 조회", description = "그룹 내 개인 랭킹을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "랭킹 정보 응답"),
            @ApiResponse(responseCode = "409", description = "신규 생성되어 랭킹이 산정되지 않는 주차에 대한 조회 요청"),
            @ApiResponse(responseCode = "404", description = "그룹을 찾을 수 없습니다.")
    })
    @GetMapping("/ranking")
    public ResponseEntity<GroupRanking> getGroupRanking(
            @Parameter(description = "조회하고자 하는 그룹의 고유 id 값", required = true) @RequestParam Long groupId,
            @Parameter(description = "랭킹을 조회하고자 하는 주차 월요일에 해당하는 날짜. 주어지지 않을 경우 가장 최근 주차의 랭킹으로 응답.") @RequestParam(value = "localDate", required = false) @DateTimeFormat(iso = DATE) LocalDate localDate) {

        if (localDate != null) {
            localDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).minusWeeks(1);
        }
        GroupEntity group = groupService.findById(groupId);

        // 해당 그룹이 생성된 날짜
        LocalDate createdDate = group.getCreatedAt().toLocalDate();

        if (!createdDate.isBefore(localDate)) {
            return ResponseEntity.status(409).build();
        }
        try {
            return ResponseEntity.ok(groupRankingService.findRankingByGroupIdAndDate(groupId, localDate));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
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
        try {
            if (groupService.isMemberOfGroup(groupId, userId)) {
                GroupInfoResponse response = groupService.getGroupInfoForMember(groupId);
                return ResponseEntity.ok(response);
            } else {
                if (groupService.findById(groupId).isPrivate()) {
                    GroupInfoResponse response = new GroupInfoResponse(false);
                    return ResponseEntity.status(409).body(response);
                } else {
                    GroupInfoResponse response = groupService.getGroupInfoForNonMember(groupId);
                    return ResponseEntity.ok(response);
                }
            }
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
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
        try {
            List<UserResponseDto> dtoList = groupService.findAllUsers(groupId);
            return ResponseEntity.ok(dtoList);
        } catch (NoSuchElementException e1) {
            return ResponseEntity.notFound().build();
        }
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
        try {
            List<BatonStatusDto> dtoList = batonService.getBatonStatuses(userId, groupId);
            return ResponseEntity.ok(dtoList);
        } catch (NoSuchElementException e1) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "바통 찌르기", description = "오늘 운동하지 않은 유저에게 바통 찌르기 알림을 보냅니다.")
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "바통 찌르기가 완료되었습니다."),
                    @ApiResponse(responseCode = "400", description = "FirebaseMessagingException"),
                    @ApiResponse(responseCode = "404", description = "User Not Found")
            })
    @PostMapping(value = "/mates/baton")
    public ResponseEntity<Boolean> turnOverBaton(
            @RequestParam("senderId") @Parameter(required = true, description = "바통을 찌른 유저의 id") Long senderId,
            @RequestParam("receiverId") @Parameter(required = true, description = "바통을 찔린 유저의 id") Long receiverId) {
        try {
            Boolean wasTurned = batonService.turnOverBaton(senderId, receiverId);
            return ResponseEntity.ok(wasTurned);
        } catch (NoSuchElementException e1) {
            return ResponseEntity.notFound().build();
        } catch (FirebaseMessagingException e2) {
            return ResponseEntity.badRequest().build();
        }
    }
}
