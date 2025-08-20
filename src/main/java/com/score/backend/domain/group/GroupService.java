package com.score.backend.domain.group;

import com.score.backend.config.ImageUploadService;
import com.score.backend.domain.exercise.ExerciseService;
import com.score.backend.domain.group.repositories.GroupRepository;
import com.score.backend.domain.group.repositories.UserGroupRepository;
import com.score.backend.domain.notification.NotificationService;
import com.score.backend.domain.notification.NotificationType;
import com.score.backend.domain.rank.RankingService;
import com.score.backend.dtos.*;
import com.score.backend.domain.user.User;
import com.score.backend.domain.user.repositories.UserRepository;
import com.score.backend.exceptions.ExceptionType;
import com.score.backend.exceptions.NotFoundException;
import com.score.backend.exceptions.ScoreCustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class GroupService {
    private final UserGroupRepository userGroupRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final ExerciseService exerciseService;
    private final RankingService rankingService;
    private final ImageUploadService imageUploadService;
    private final NotificationService notificationService;

    @Transactional(readOnly = true)
    public GroupEntity findById(Long id) {
        return groupRepository.findById(id).orElseThrow(
                () -> new NotFoundException(ExceptionType.GROUP_NOT_FOUND)
        );
    }

    @Transactional(readOnly = true)
    public boolean checkEmptySpaceExistence(Long id) {
        GroupEntity group = findById(id);
        return group.getMembers().size() < group.getUserLimit();
    }

    public List<GroupEntity> findAll() {
        return groupRepository.findAll();
    }

    public GroupEntity createGroup(User admin, GroupCreateDto groupCreateDto, MultipartFile image) throws IOException {
        GroupEntity group = GroupEntity.builder()
                .groupName(groupCreateDto.getGroupName())
                .belongingSchool(admin.getSchool())
                .groupImg(imageUploadService.uploadImage(image))
                .groupDescription(groupCreateDto.getGroupDescription())
                .userLimit(groupCreateDto.getUserLimit())
                .isPrivate(groupCreateDto.isPrivate())
                .groupPassword(groupCreateDto.getGroupPassword())
                .admin(admin)
                .cumulativeTime(0.0)
                .build();
        UserGroup groupUser = new UserGroup(admin, group);
        userGroupRepository.save(groupUser);
        group.getMembers().add(groupUser);
        GroupEntity generatedGroup = groupRepository.save(group);
        admin.getSchool().getGroups().add(generatedGroup);
        return generatedGroup;
    }

    public void leaveGroup(Long groupId, User user) {
        UserGroup userGroup = userGroupRepository.findByUserIdAndGroupId(user.getId(), groupId);
        if (userGroup == null) {
            throw new NotFoundException(ExceptionType.USER_GROUP_NOT_FOUND);
        }
        GroupEntity group = findById(groupId);
        if (group.getAdmin().equals(user)) {
            throw new ScoreCustomException(ExceptionType.ADMIN_GROUP_LEAVING);
        }

        group.getMembers().remove(userGroup);
        user.getUserGroups().remove(userGroup);
        userGroupRepository.delete(userGroup);
    }

    // 비밀번호 일치 여부 확인
    public boolean verifyGroupPassword(String inputPassword, Long groupId) {
        GroupEntity group = findById(groupId);
        return group.getGroupPassword().equals(inputPassword);
    }

    // 방장에게 그룹 가입 신청 알림 보내기
    public void sendGroupJoinRequestNotification(GroupJoinRequest groupJoinRequest, User requester) {
        GroupEntity group = findById(groupJoinRequest.getGroupId());
        User admin = group.getAdmin();
        NotificationDto dto = NotificationDto.builder()
                .sender(requester)
                .receiver(admin)
                .relatedGroup(group)
                .type(NotificationType.JOIN_REQUEST)
                .body(groupJoinRequest.getMessage())
                .build();
        notificationService.sendAndSaveNotification(dto);
    }

    // 그룹 내 메이트 목록 전체 조회
    public List<UserGroup> findAllUsers(Long groupId) {
        return findById(groupId).getMembers();
    }

    public List<UserResponseDto> convertToUserResponseDto(Long groupId) {
        List<UserGroup> userGroups = findAllUsers(groupId);
        List<UserResponseDto> dtos = new ArrayList<>();
        for (UserGroup userGroup : userGroups) {
            dtos.add(new UserResponseDto(userGroup.getMember().getId(), userGroup.getMember().getNickname(), userGroup.getMember().getProfileImg()));
        }
        return dtos;
    }

    // 그룹에 새로운 멤버 추가
    public void addNewMember(Long groupId, User user) {
        GroupEntity group = findById(groupId);
        if (userGroupRepository.findByUserIdAndGroupId(user.getId(), groupId) == null) {
            if (!group.getBelongingSchool().getId().equals(user.getSchool().getId())) {
                throw new ScoreCustomException(ExceptionType.USERS_SCHOOL_GROUP_UNMATCHED);
            }
            UserGroup userGroup = new UserGroup(user, group);
            userGroupRepository.save(userGroup);
            group.getMembers().add(userGroup);
            user.addGroup(userGroup, group);
            NotificationDto dto = NotificationDto.builder()
                    .receiver(user)
                    .relatedGroup(group)
                    .type(NotificationType.JOIN_COMPLETE)
                    .build();
            notificationService.sendAndSaveNotification(dto);
            return;
        }
        throw new ScoreCustomException(ExceptionType.ALREADY_JOINED_GROUP);
    }

    // 유저가 속한 모든 그룹의 누적 운동 시간 증가
    public void increaseCumulativeTime(User agent, Double duration) {
        List<GroupEntity> groups = this.findAllGroupsByUserId(agent.getId());
        if (!groups.isEmpty()) {
            for (GroupEntity group : groups) {
                group.updateCumulativeTime(duration);
            }
        }
    }

    // 유저가 가입한 모든 그룹의 목록 조회
    @Transactional(readOnly = true)
    public List<GroupEntity> findAllGroupsByUserId(Long userId) {
        List<UserGroup> userGroups = userGroupRepository.findByUserId(userId);
        List<GroupEntity> groups = new ArrayList<>();
        for (UserGroup userGroup : userGroups) {
            groups.add(userGroup.getGroup());
        }
        return groups;
    }

    public List<User> findAllUsersDidExerciseToday(Long groupId) {
        return userRepository.findGroupMatesWhoDidExerciseToday(groupId);
    }

    // 해당 유저가 그룹의 멤버인지 여부 확인
    @Transactional(readOnly = true)
    public boolean isMemberOfGroup(Long groupId, Long userId) {
        GroupEntity group = findById(groupId);
        return group.getMembers().contains(userGroupRepository.findByUserIdAndGroupId(userId, groupId));
    }

    // 유저가 가입해 있지 않은 그룹의 정보 반환
    @Transactional(readOnly = true)
    public GroupInfoResponse getGroupInfoForNonMember(Long groupId) {
        GroupEntity group = findById(groupId);
        Page<FeedInfoResponse> feeds = exerciseService.getGroupsAllExercisePics(0, groupId);
        return new GroupInfoResponse(group.getGroupName(), group.getUserLimit(), group.getCumulativeTime(), rankingService.getRatioOfParticipate(group), group.getGroupImg(), group.isPrivate(), group.getMembers().size(), findAllUsersDidExerciseToday(groupId).size(), feeds);
    }

    // 유저가 가입해 있는 그룹의 정보 반환
    @Transactional(readOnly = true)
    public GroupInfoResponse getGroupInfoForMember(Long groupId) {
        GroupEntity group = findById(groupId);
        Page<FeedInfoResponse> feeds = exerciseService.getGroupsAllExercises(0, groupId);
        return new GroupInfoResponse(group.getGroupName(), group.getUserLimit(), group.getCumulativeTime(), rankingService.getRatioOfParticipate(group), group.getGroupImg(), group.isPrivate(), group.getMembers().size(), findAllUsersDidExerciseToday(groupId).size(), feeds);
    }
}
