package com.score.backend.domain.group;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.score.backend.config.ImageUploadService;
import com.score.backend.domain.exercise.ExerciseService;
import com.score.backend.domain.group.repositories.GroupRepository;
import com.score.backend.domain.group.repositories.UserGroupRepository;
import com.score.backend.domain.notification.NotificationService;
import com.score.backend.domain.rank.RankingService;
import com.score.backend.domain.user.UserService;
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
import java.time.LocalDateTime;
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
    private final UserService userService;
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

    public GroupEntity createGroup(GroupCreateDto groupCreateDto, MultipartFile image) throws IOException {

        User admin = userRepository.findById(groupCreateDto.getAdminId())
                .orElseThrow(() -> new NotFoundException(ExceptionType.USER_NOT_FOUND));

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

//    public void updateGroup(Long groupId, GroupCreateDto groupCreateDto, Long adminId) {
//        GroupEntity group = groupRepository.findById(groupId)
//                .orElseThrow(() -> new IllegalArgumentException("그룹을 찾을 수 없습니다."));
//
//        if (!group.getAdmin().getId().equals(adminId)) {
//            throw new IllegalArgumentException("권한이 없습니다.");
//        }
//
//        group.setGroupImg(groupCreateDto.getGroupImg());
//        group.setGroupName(groupCreateDto.getGroupName());
//        group.setGroupDescription(groupCreateDto.getGroupDescription());
//        group.setUserLimit(groupCreateDto.getUserLimit());
//        group.setPrivate(groupCreateDto.isPrivate());
//        group.setGroupPassword(groupCreateDto.getGroupPassword());
//
//        groupRepository.save(group);
//    }

    public void leaveGroup(Long groupId, Long userId) {
        UserGroup userGroup = userGroupRepository.findByUserIdAndGroupId(userId, groupId);
        if (userGroup == null) {
            throw new NotFoundException(ExceptionType.USER_GROUP_NOT_FOUND);
        }
        GroupEntity group = findById(groupId);
        User user = userService.findUserById(userId);
        if (group.getAdmin().equals(user)) {
            throw new ScoreCustomException(ExceptionType.ADMIN_GROUP_LEAVING);
        }

        group.getMembers().remove(userGroup);
        user.getUserGroups().remove(userGroup);
        userGroupRepository.delete(userGroup);
    }

    public void removeMember(Long groupId, Long userId) {
        GroupEntity group = findById(groupId);
        User user = userService.findUserById(userId);
        Long adminId = group.getAdmin().getId(); //그룹의 adminId 가져오기

        if (!group.getAdmin().getId().equals(adminId)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        if (user.getId().equals(adminId)) {
            throw new IllegalArgumentException("방장은 강퇴할 수 없습니다.");
        }

        if (!group.getMembers().contains(user)) {
            throw new IllegalArgumentException("그룹에 속해 있지 않습니다.");
        }

        group.getMembers().remove(user); // 그룹의 멤버 목록에서 사용자 제거
        //user.setGroup(null); // 사용자와 그룹의 연관 관계 해제
        groupRepository.save(group);
        userRepository.save(user);
    }

    // 비밀번호 일치 여부 확인
    public boolean verifyGroupPassword(String inputPassword, Long groupId) {
        GroupEntity group = findById(groupId);
        return group.getGroupPassword().equals(inputPassword);
    }

    // 방장에게 그룹 가입 신청 알림 보내기
    public void sendGroupJoinRequestNotification(Long groupId, Long userId) throws FirebaseMessagingException {
        User requester = userService.findUserById(userId);
        GroupEntity group = findById(groupId);
        User admin = group.getAdmin();
        FcmMessageRequest message = new FcmMessageRequest(admin.getId(), requester.getNickname() + "님이 " + group.getGroupName() + "에 가입을 신청했어요!", "알림 페이지에서 가입을 승인 혹은 거절할 수 있어요.");
        notificationService.sendMessage(message);
        notificationService.saveNotification(message);
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
    public void addNewMember(Long groupId, Long userId) throws FirebaseMessagingException {
        GroupEntity group = findById(groupId);
        User user = userService.findUserById(userId);
        if (userGroupRepository.findByUserIdAndGroupId(userId, groupId) == null) {
            if (!group.getBelongingSchool().getId().equals(user.getSchool().getId())) {
                throw new ScoreCustomException(ExceptionType.USERS_SCHOOL_GROUP_UNMATCHED);
            }
            UserGroup userGroup = new UserGroup(user, group);
            userGroupRepository.save(userGroup);
            group.getMembers().add(userGroup);
            user.addGroup(userGroup, group);
            FcmMessageRequest message = new FcmMessageRequest(userId, group.getGroupName() + "에 가입이 승인되었어요!", "어서 확인해보세요.");
            notificationService.sendMessage(message);
            notificationService.saveNotification(message);
            return;
        }
        throw new ScoreCustomException(ExceptionType.ALREADY_JOINED_GROUP);
    }

    // 유저가 속한 모든 그룹의 누적 운동 시간 증가
    public void increaseCumulativeTime(Long userId, LocalDateTime start, LocalDateTime end) {
        List<GroupEntity> groups = this.findAllGroupsByUserId(userId);
        if (!groups.isEmpty()) {
            double duration = exerciseService.calculateExerciseDuration(start, end);
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
