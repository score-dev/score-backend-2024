package com.score.backend.domain.user;

import com.score.backend.domain.notification.Notification;
import com.score.backend.domain.notification.NotificationRepository;
import com.score.backend.domain.rank.RankingService;
import com.score.backend.domain.rank.group.GroupRanker;
import com.score.backend.domain.rank.group.GroupRankerRepository;
import com.score.backend.domain.user.repositories.UserRepository;
import com.score.backend.dtos.GroupMateTodaysExerciseDto;
import com.score.backend.dtos.UserUpdateDto;
import com.score.backend.config.ImageUploadService;
import com.score.backend.domain.school.SchoolService;
import com.score.backend.exceptions.ExceptionType;
import com.score.backend.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ImageUploadService imageUploadService;
    private final SchoolService schoolService;
    private final RankingService rankingService;
    private final NotificationRepository notificationRepository;
    private final GroupRankerRepository groupRankerRepository;

    public User findDummyUser() {
        return userRepository.findByNickname("(알 수 없음)").orElseThrow(
                () -> new RuntimeException(ExceptionType.DUMMY_USER_NOT_FOUND.getMessage()));
    }

    @Transactional
    public Long saveUser(User user, int profileImgId, MultipartFile profileImage) throws IOException {
        if (profileImgId != 0 || profileImage == null) {
            user.setProfileImageUrl(DefaultProfileImg.getUrlById(profileImgId));
        } else {
            user.setProfileImageUrl(imageUploadService.uploadImage(profileImage));
        }
        return userRepository.save(user).getId();
    }

    @Transactional
    public void updateUser(Long userId, UserUpdateDto userUpdateDto, MultipartFile profileImage) throws IOException {
        User user = this.findUserById(userId);
        if (profileImage != null) {
            user.setProfileImageUrl(imageUploadService.uploadImage(profileImage));
        }
        if (userUpdateDto.getNickname() != null) {
            user.setNickname(userUpdateDto.getNickname());
        }
        if (userUpdateDto.getGoal() != null) {
            user.setGoal(userUpdateDto.getGoal());
        }
        if (userUpdateDto.getHeight() != 0) {
            user.setHeight(userUpdateDto.getHeight());
        }
        if (userUpdateDto.getWeight() != 0) {
            user.setWeight(userUpdateDto.getWeight());
        }
        if (userUpdateDto.getGrade() != 0) {
            user.setGrade(userUpdateDto.getGrade());
        }
        if (userUpdateDto.getSchool() != null && !user.getSchool().getSchoolCode().equals(userUpdateDto.getSchool().getSchoolCode())) {
            user.setSchoolAndStudent(schoolService.findOrSave(userUpdateDto.getSchool()));
        }
    }

    @Transactional
    public void withdrawUser(Long id) throws SQLIntegrityConstraintViolationException {
        User deletingUser = findUserById(id);
        User dummyUser = findDummyUser();
        List<GroupRanker> rankerInfos = rankingService.findGroupRankersByUserId(deletingUser.getId());
        for (GroupRanker rankerInfo : rankerInfos) {
            rankerInfo.setUser(dummyUser);
            groupRankerRepository.save(rankerInfo);
        }
        List<Notification> sentNotifications = notificationRepository.findBySenderId(deletingUser.getId());
        for (Notification sentNotification : sentNotifications) {
            sentNotification.setSender(dummyUser);
            notificationRepository.save(sentNotification);
        }
        userRepository.delete(deletingUser);
    }


    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new NotFoundException(ExceptionType.USER_NOT_FOUND));
    }

    public List<User> findUsersByGoal(LocalTime curr) {
        return userRepository.findAllByGoal(curr);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findUserByNickname(String nickname) {
        return userRepository.findByNickname(nickname);
    }

    public User findUserByLoginKey(String sub) {
        return userRepository.findByLoginKey(sub).orElseThrow(
                () -> new NotFoundException(ExceptionType.USER_NOT_FOUND));
    }

    public Long isPresentUser(String key) {
        Optional<User> userOption = userRepository.findByLoginKey(key);
        if (userOption.isPresent()) {
            return userOption.get().getId();
        }
        return -1L;
    }

    public List<GroupMateTodaysExerciseDto> findGroupMateTodaysExerciseByGroupId(Long groupId) {
        return userRepository.findGroupMatesTodaysExercises(groupId);
    }
}
