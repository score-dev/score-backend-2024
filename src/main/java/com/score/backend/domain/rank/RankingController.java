package com.score.backend.domain.rank;

import com.score.backend.domain.group.GroupEntity;
import com.score.backend.domain.group.GroupService;
import com.score.backend.domain.rank.group.GroupRankService;
import com.score.backend.domain.rank.group.GroupRanking;
import com.score.backend.domain.rank.school.SchoolRankService;
import com.score.backend.domain.school.School;
import com.score.backend.domain.school.SchoolService;
import com.score.backend.domain.user.UserService;
import com.score.backend.dtos.schoolrank.SchoolRankerResponse;
import com.score.backend.dtos.schoolrank.SchoolRankingResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.NoSuchElementException;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

@Tag(name = "Ranking", description = "그룹 랭킹 및 학교 랭킹 관리를 위한 API입니다.")
@RestController
@RequestMapping("/score/ranking")
@RequiredArgsConstructor
public class RankingController {
    private final GroupService groupService;
    private final UserService userService;
    private final SchoolService schoolService;
    private final GroupRankService groupRankService;
    private final SchoolRankService schoolRankService;

    @Operation(summary = "그룹 랭킹 조회", description = "그룹 내 개인 랭킹을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "랭킹 정보 응답"),
            @ApiResponse(responseCode = "409", description = "신규 생성되어 랭킹이 산정되지 않는 주차에 대한 조회 요청"),
            @ApiResponse(responseCode = "404", description = "그룹을 찾을 수 없습니다.")
    })
    @GetMapping("/group")
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
            return ResponseEntity.ok(groupRankService.findRankingByGroupIdAndDate(groupId, localDate));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "학교 랭킹 조회", description = "학교 내 그룹 랭킹을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "랭킹 정보 응답"),
            @ApiResponse(responseCode = "404", description = "학교를 찾을 수 없습니다.")
    })
    @GetMapping("/school")
    public ResponseEntity<SchoolRankingResponse> getSchoolRanking(
            @Parameter(description = "조회를 요청한 유저의 고유 Id 값", required = true) @RequestParam Long userId,
            @Parameter(description = "랭킹을 조회하고자 하는 주차 월요일에 해당하는 날짜. 주어지지 않을 경우 가장 최근 주차의 랭킹으로 응답.") @RequestParam(value = "localDate", required = false) @DateTimeFormat(iso = DATE) LocalDate localDate) {
        if (localDate != null) {
            localDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).minusWeeks(1);
        }
        try {
            School school = userService.findUserById(userId).get().getSchool();
            List<SchoolRankerResponse> allRankers = schoolRankService.findAllSchoolRankingByUserId(userId, localDate);
            List<SchoolRankerResponse> myGroupRankers = schoolRankService.findMyGroupRankingByUserId(userId, localDate);
            return ResponseEntity.ok(new SchoolRankingResponse(school.getSchoolName(), school.getId(), allRankers, myGroupRankers));

        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
