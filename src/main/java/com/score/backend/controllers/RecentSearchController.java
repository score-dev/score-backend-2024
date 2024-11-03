package com.score.backend.controllers;

import com.score.backend.services.RecentSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recent-searches")
@RequiredArgsConstructor
public class RecentSearchController {

    private final RecentSearchService recentSearchService;

    // 최근 검색어 추가
    @PostMapping("/{userId}")
    public ResponseEntity<String> addRecentSearch(@PathVariable Long userId, @RequestParam String keyword) {
        recentSearchService.addRecentSearch(userId, keyword);
        return ResponseEntity.ok("검색어가 추가되었습니다.");
    }

    // 최근 검색어 조회
    @GetMapping("/{userId}")
    public ResponseEntity<List<Object>> getRecentSearches(@PathVariable Long userId) {
        List<Object> recentSearches = recentSearchService.getRecentSearches(userId);
        return ResponseEntity.ok(recentSearches);
    }

    // 특정 검색어 삭제
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteRecentSearch(@PathVariable Long userId, @RequestParam String keyword) {
        recentSearchService.deleteRecentSearch(userId, keyword);
        return ResponseEntity.ok("검색어가 삭제되었습니다.");
    }

    // 모든 최근 검색어 삭제
    @DeleteMapping("/{userId}/all")
    public ResponseEntity<String> clearRecentSearches(@PathVariable Long userId) {
        recentSearchService.clearRecentSearches(userId);
        return ResponseEntity.ok("모든 검색어가 삭제되었습니다.");
    }
}
