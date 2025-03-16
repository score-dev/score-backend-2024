package com.score.backend.domain.exercise;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.score.backend.config.ImageUploadService;
import com.score.backend.domain.exercise.repositories.ExerciseRepository;
import com.score.backend.domain.exercise.repositories.TaggedUserRepository;
import com.score.backend.domain.friend.block.BlockedUser;
import com.score.backend.domain.notification.NotificationService;
import com.score.backend.domain.user.User;
import com.score.backend.domain.user.UserService;
import com.score.backend.dtos.FcmMessageRequest;
import com.score.backend.dtos.FeedInfoResponse;
import com.score.backend.dtos.WalkingDto;
import com.score.backend.exceptions.ExceptionType;
import com.score.backend.exceptions.NotFoundException;
import com.score.backend.exceptions.ScoreCustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class  ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final TaggedUserRepository taggedUserRepository;
    private final UserService userService;
    private final ImageUploadService imageUploadService;
    private final NotificationService notificationService;

    // user1이 user2의 피드 목록을 조회 (둘이 같을 경우 자기가 자기 피드를 조회)
    @Transactional(readOnly = true)
    public Page<FeedInfoResponse> getUsersAllExercises(int page, Long id1, Long id2) {
        User user1 = userService.findUserById(id1);
        User user2 = userService.findUserById(id2);

        if (user1.getBlockedUsers().stream().map(BlockedUser::getBlocked).toList().contains(user2)) {
            throw new ScoreCustomException(ExceptionType.ACCESS_TO_BLOCKED_USER);
        }
        Pageable pageable = PageRequest.of(page, 9, Sort.by(Sort.Order.desc("completedAt")));
        return new FeedInfoResponse().toDtoListForMates(exerciseRepository.findExercisePageByUserId(id2, pageable));
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
    public List<Exercise> getWeeklyExercises(Long userId) {
        return exerciseRepository.findUsersWeeklyExercises(userId, LocalDate.now());
    }

    public void saveFeed(WalkingDto walkingDto, MultipartFile multipartFile) throws FirebaseMessagingException, IOException {
        // 새로운 피드 엔티티 생성
        Exercise feed = walkingDto.toEntity();
        // 운동한 유저(피드 작성자) db에서 찾기
        User agent = userService.findUserById(walkingDto.getAgentId());

        // agent와 함께 운동한 유저의 id 값을 가지고 db에서 찾기
        Set<TaggedUser> taggedUsers = new HashSet<>();
        if (walkingDto.getOthersId() != null) {
            for (Long id : walkingDto.getOthersId()) {
                if (id.equals(agent.getId())) {
                    continue;
                }
                User user = userService.findUserById(id);
                TaggedUser taggedUser = new TaggedUser(feed, user);
                taggedUserRepository.save(taggedUser);
                taggedUsers.add(taggedUser);
                // 태그된 유저들에게 알림 전송 및 알림 저장
                if (user.isTag()) {
                    FcmMessageRequest fcmMessageRequest = new FcmMessageRequest(user.getId(), agent.getNickname() + "님에게 함께 운동한 사람으로 태그되었어요!", "피드를 확인해보러 갈까요?");
                    notificationService.sendMessage(fcmMessageRequest);
                    notificationService.saveNotification(fcmMessageRequest);
                }
            }
        }
        // 피드 작성자, 함께 운동한 친구 설정
        feed.setAgentAndExerciseUser(agent, taggedUsers);
        // 프로필 사진 설정
        feed.setExercisePicUrl(imageUploadService.uploadImage(multipartFile));
        exerciseRepository.save(feed);
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
    public void cumulateExerciseDuration(Long userId, LocalDateTime start, LocalDateTime end) {
        User user = userService.findUserById(userId);
        user.updateCumulativeTime(calculateExerciseDuration(start, end));
    }

    // 유저의 운동 거리 누적
    public void cumulateExerciseDistance(Long userId, double distance) {
        User user = userService.findUserById(userId);
        user.updateCumulativeDistance(distance);
    }

    // 유저의 연속 운동 일수 1 증가
    public void increaseConsecutiveDate(Long userId) {
        User user = userService.findUserById(userId);
        user.updateConsecutiveDate(true);
    }

    // 유저의 마지막 운동 시간 및 날짜 설정
    public void updateLastExerciseDateTime(Long userId, LocalDateTime lastExerciseDateTime) {
        User user = userService.findUserById(userId);
        user.updateLastExerciseDateTime(lastExerciseDateTime);
    }

    // 유저의 금주 운동 횟수, 운동 시간 업데이트
    public void updateWeeklyExerciseStatus(Long userId, boolean needToIncrease, LocalDateTime start, LocalDateTime end) {
        User user = userService.findUserById(userId);
        user.updateWeeklyExerciseStatus(needToIncrease, calculateExerciseDuration(start, end));
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
    public boolean isValidateExercise(LocalDateTime start, LocalDateTime end) {
        return calculateExerciseDuration(start, end) >= 180;
    }

    // 오늘 처음으로 3분 이상 운동했는지 여부 확인
    @Transactional(readOnly = true)
    public boolean isTodaysFirstValidateExercise(Long userId) {
        return exerciseRepository.countUsersValidateExerciseToday(userId, LocalDate.now()) == 0;
    }
}
