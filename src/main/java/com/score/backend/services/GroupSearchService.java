package com.score.backend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupSearchService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final int MAX_RECENT_SEARCHES = 5;

    // 최근 검색어 추가
    public void addRecentSearch(Long userId, String keyword) {
        String key = "user:" + userId + ":recent_searches";

        // 새로운 검색어를 리스트에 추가
        redisTemplate.opsForList().leftPush(key, keyword);

        // 리스트 길이를 5로 제한 (최신 5개만 유지)
        redisTemplate.opsForList().trim(key, 0, MAX_RECENT_SEARCHES - 1);
    }

    // 최근 검색어 조회
    public List<Object> getRecentSearches(Long userId) {
        String key = "user:" + userId + ":recent_searches";
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    // 최근 검색어 삭제
    public void deleteRecentSearch(Long userId, String keyword) {
        String key = "user:" + userId + ":recent_searches";
        redisTemplate.opsForList().remove(key, 1, keyword);
    }

    // 모든 최근 검색어 삭제
    public void clearRecentSearches(Long userId) {
        String key = "user:" + userId + ":recent_searches";
        redisTemplate.delete(key);
    }
}
