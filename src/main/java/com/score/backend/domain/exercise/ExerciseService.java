package com.score.backend.domain.exercise;

import com.score.backend.domain.exercise.repositories.ExerciseRepository;
import com.score.backend.domain.exercise.repositories.TaggedUserRepository;
import com.score.backend.domain.friend.block.BlockedUser;
import com.score.backend.domain.notification.NotificationService;
import com.score.backend.domain.notification.NotificationType;
import com.score.backend.domain.user.User;
import com.score.backend.dtos.FeedCalendarResponse;
import com.score.backend.dtos.FeedInfoResponse;
import com.score.backend.dtos.NotificationDto;
import com.score.backend.dtos.WalkingDto;
import com.score.backend.exceptions.ExceptionType;
import com.score.backend.exceptions.NotFoundException;
import com.score.backend.exceptions.ScoreCustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class  ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final TaggedUserRepository taggedUserRepository;
    private final NotificationService notificationService;

    // user1이 user2의 피드 목록을 조회 (둘이 같을 경우 자기가 자기 피드를 조회)
    @Transactional(readOnly = true)
    public Page<FeedInfoResponse> getUsersAllExercises(int page, User user1, User user2) {
        if (user1.getBlockedUsers().stream().map(BlockedUser::getBlocked).toList().contains(user2)) {
            throw new ScoreCustomException(ExceptionType.ACCESS_TO_BLOCKED_USER);
        }
        Pageable pageable = PageRequest.of(page, 9, Sort.by(Sort.Order.desc("completedAt")));
        return new FeedInfoResponse().toDtoListForMates(exerciseRepository.findExercisePageByUserId(user2.getId(), pageable));
    }

    // 마이페이지 캘린더 기록 조회
    @Transactional(readOnly = true)
    public List<FeedCalendarResponse> getUsersMonthlyExercises(Long userId, int year, int month) {
        List<FeedCalendarResponse> responses = new ArrayList<>();
        exerciseRepository.findUsersMonthlyExercises(userId, year, month).forEach(
                e -> responses.add(new FeedCalendarResponse(e.getId(), e.getStartedAt(), e.getCompletedAt()))
        );
        return responses;
    }

    // 그룹 전체 피드 조회
    @Transactional(readOnly = true)
    public Page<FeedInfoResponse> getGroupsAllExercises(int page, Long groupId) {
        Pageable pageable = PageRequest.of(page, 9, Sort.by(Sort.Order.desc("completedAt")));
        return new FeedInfoResponse().toDtoListForMates(exerciseRepository.findExercisePageByGroupId(groupId, pageable));
    }

    @Transactional(readOnly = true)
    public Page<FeedInfoResponse> getGroupsAllExercisePics(int page, Long groupId) {
        Pageable pageable = PageRequest.of(page, 9, Sort.by(Sort.Order.desc("completedAt")));
        return new FeedInfoResponse().toDtoListForNonMates(exerciseRepository.findExercisePageByGroupId(groupId, pageable));
    }

    @Transactional(readOnly = true)
    public List<Exercise> getWeeklyExercises(User user) {
        return exerciseRepository.findUsersWeeklyExercises(user.getId(), LocalDate.now());
    }

    public Exercise saveFeed(User agent, Set<TaggedUser> taggedUsers, WalkingDto walkingDto, String imgUrl, Double duration) {
        // 새로운 피드 엔티티 생성
        Exercise feed = walkingDto.toEntity(imgUrl, duration);
        // 피드 작성자, 함께 운동한 친구 설정
        feed.setAgentAndExerciseUser(agent, taggedUsers);
        exerciseRepository.save(feed);
        return feed;
    }

    public Set<TaggedUser> findTaggedUsers(User agent, List<User> otherUsers) {
        Set<TaggedUser> taggedUsers = new HashSet<>();
        for (User user : otherUsers) {
            if (user.equals(agent)) {
                continue;
            }
            TaggedUser taggedUser = new TaggedUser(user);
            taggedUserRepository.save(taggedUser);
            taggedUsers.add(taggedUser);
        }
        return taggedUsers;
    }

    public void deleteFeed(Exercise exercise) {
        exerciseRepository.delete(exercise);
    }

    @Transactional(readOnly = true)
    public Exercise findFeedByExerciseId(Long exerciseId) {
        return exerciseRepository.findById(exerciseId).orElseThrow(
                () -> new NotFoundException(ExceptionType.FEED_NOT_FOUND)
        );
    }

    // 유저의 운동 시간 누적
    public void cumulateExerciseDuration(User user, Double duration) {
        user.updateCumulativeTime(duration);
    }

    // 유저의 운동 거리 누적
    public void cumulateExerciseDistance(User user, double distance) {
        user.updateCumulativeDistance(distance);
    }

    // 유저의 연속 운동 일수 1 증가
    public void increaseConsecutiveDate(User user) {
        user.updateConsecutiveDate(true);
    }

    // 유저의 마지막 운동 시간 및 날짜 설정
    public void updateLastExerciseDateTime(User user, LocalDateTime lastExerciseDateTime) {
        user.updateLastExerciseDateTime(lastExerciseDateTime);
    }

    // 유저의 금주 운동 횟수, 운동 시간 업데이트
    public void updateWeeklyExerciseStatus(User user, boolean needToIncrease, Double duration) {
        user.updateWeeklyExerciseStatus(needToIncrease, duration);
    }

    // 운동한 시간 계산
    @Transactional(readOnly = true)
    public double calculateExerciseDuration(LocalDateTime start, LocalDateTime end) {
        Duration duration = Duration.between(start, end);
        if (duration.getSeconds() < 0) {
            throw new ScoreCustomException(ExceptionType.EXERCISE_TIME_ERROR);
        }
        return duration.getSeconds();
    }

    // 3분 이상 운동했는지 여부 확인
    @Transactional(readOnly = true)
    public boolean isValidateExercise(Double duration) {
        return duration >= 180;
    }

    // 오늘 처음으로 3분 이상 운동했는지 여부 확인
    @Transactional(readOnly = true)
    public boolean isTodaysFirstValidateExercise(User user) {
        return exerciseRepository.countUsersValidateExerciseToday(user.getId(), LocalDate.now()) == 0;
    }

    public void notifyToTaggedUsers(Set<TaggedUser> taggedUsers, Exercise feed) {
        for (TaggedUser taggedUser : taggedUsers) {
            // 태그된 유저들에게 알림 전송 및 알림 저장
            if (taggedUser.getUser().isTag()) {
                NotificationDto dto = NotificationDto.builder()
                        .sender(feed.getAgent())
                        .receiver(taggedUser.getUser())
                        .relatedFeed(feed)
                        .type(NotificationType.TAGGED)
                        .build();
                notificationService.sendAndSaveNotification(dto);
            }
        }
    }
}
