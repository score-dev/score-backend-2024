package com.score.backend.controllers;

import com.score.backend.models.dtos.GroupDto;
import com.score.backend.services.GroupSearchService;
import com.score.backend.services.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/score/school")
@RequiredArgsConstructor
public class GroupSearchController {
    private final GroupService groupService;

    @Operation(summary = "그룹 검색", description = "그룹 이름을 통해 같은 학교 내 그룹을 검색합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "그룹 검색 완료"),
            @ApiResponse(responseCode = "404", description = "검색 결과가 없습니다.")
    })
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchGroups(@RequestParam String schoolCode, @RequestParam String keyword) {
        List<GroupDto> groupDtos = groupService.searchingGroups(schoolCode, keyword);

        Map<String, Object> response = new HashMap<>();
        if (!groupDtos.isEmpty()) {
            response.put("groups", groupDtos);
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "해당 그룹은 존재하지 않아요");
            return ResponseEntity.status(404).body(response);
        }
    }
    // 최신순 추천 그룹 목록 조회
    @GetMapping("/recommend/recent")
    public ResponseEntity<List<GroupDto>> getRecentGroups(@RequestParam String schoolCode) {
        List<GroupDto> recentGroups = groupService.getRecentGroupsBySchool(schoolCode);
        return ResponseEntity.ok(recentGroups);
    }

    private final GroupSearchService groupSearchService;

    // 최근 검색어 추가
    @PostMapping("/{userId}")
    public ResponseEntity<String> addRecentSearch(@PathVariable Long userId, @RequestParam String keyword) {
        groupSearchService.addRecentSearch(userId, keyword);
        return ResponseEntity.ok("검색어가 추가되었습니다.");
    }

    // 최근 검색어 조회
    @GetMapping("/{userId}")
    public ResponseEntity<List<Object>> getRecentSearches(@PathVariable Long userId) {
        List<Object> recentSearches = groupSearchService.getRecentSearches(userId);
        return ResponseEntity.ok(recentSearches);
    }

    // 특정 검색어 삭제
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteRecentSearch(@PathVariable Long userId, @RequestParam String keyword) {
        groupSearchService.deleteRecentSearch(userId, keyword);
        return ResponseEntity.ok("검색어가 삭제되었습니다.");
    }

    // 모든 최근 검색어 삭제
    @DeleteMapping("/{userId}/all")
    public ResponseEntity<String> clearRecentSearches(@PathVariable Long userId) {
        groupSearchService.clearRecentSearches(userId);
        return ResponseEntity.ok("모든 검색어가 삭제되었습니다.");
    }
}
