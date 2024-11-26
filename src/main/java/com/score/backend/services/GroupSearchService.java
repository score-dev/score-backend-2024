package com.score.backend.services;

import com.score.backend.models.GroupEntity;
import com.score.backend.models.School;
import com.score.backend.models.dtos.GroupDto;
import com.score.backend.repositories.group.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupSearchService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final SchoolService schoolService;
    private final GroupRepository groupRepository;

    private static final int MAX_RECENT_SEARCHES = 5;

    // 검색 결과 가져오기
    public List<GroupDto> getGroupSearchResult(Long schoolId, String keyword) {
        List<GroupEntity> searchResult = groupRepository.findGroupsByKeyword(schoolId, keyword);
        List<GroupDto> resultDto = new ArrayList<>();
        for (GroupEntity groupEntity : searchResult) {
            resultDto.add(GroupDto.fromEntity(groupEntity));
        }
        return resultDto;
    }

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

    // 최신순 그룹 추천
    @Transactional(readOnly = true)
    public List<GroupDto> getRecentGroupsBySchool(String schoolCode) {
        // 학교 코드로 학교 조회
        School school = schoolService.findSchoolByCode(schoolCode);
        if (school == null) {
            throw new IllegalArgumentException("해당 학교를 찾을 수 없습니다.");
        }

        // 최신순 그룹 조회
        List<GroupEntity> recentGroups = groupRepository.findByBelongingSchoolId(school.getId());

        // GroupEntity 리스트를 GroupDto 리스트로 변환하여 반환
        return recentGroups.stream()
                .map(GroupDto::fromEntity)
                .collect(Collectors.toList());
    }
}
