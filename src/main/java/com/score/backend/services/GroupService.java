package com.score.backend.services;

import com.score.backend.models.GroupEntity;
import com.score.backend.models.dtos.*;
import com.score.backend.models.User;
import com.score.backend.repositories.group.GroupRepository;
import com.score.backend.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class GroupService{

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final ExerciseService exerciseService;
    private final UserService userService;
    private final SchoolRankingService schoolRankingService;
    private final NotificationService notificationService;

    @Transactional(readOnly = true)
    public GroupEntity findById(Long id){
        return groupRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Group not found")
        );
    }

    public List<GroupEntity> findAll() {
        return groupRepository.findAll();
    }


    public void createGroup(GroupCreateDto groupCreateDto, Long adminId) {

        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("그룹장을 못 찾습니다."));

        GroupEntity group = GroupEntity.builder()
                .groupImg(groupCreateDto.getGroupImg())
                .groupName(groupCreateDto.getGroupName())
                .groupDescription(groupCreateDto.getGroupDescription())
                .userLimit(groupCreateDto.getUserLimit())
                .isPrivate(groupCreateDto.isPrivate())
                .groupPassword(groupCreateDto.getGroupPassword())
                .admin(admin)
                .cumulativeTime(0.0)
                .todayExercisedCount(0)
                .build();

        groupRepository.save(group);
    }

//    public List<GroupDto> getAllGroups() {
//        List<GroupEntity> groups = groupRepository.findAll();
//        return groups.stream().map(GroupDto::fromEntity).collect(Collectors.toList());
//    }

    public void updateGroup(Long groupId, GroupCreateDto groupCreateDto, Long adminId) {
        GroupEntity group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("그룹을 찾을 수 없습니다."));

        if (!group.getAdmin().getId().equals(adminId)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        group.setGroupImg(groupCreateDto.getGroupImg());
        group.setGroupName(groupCreateDto.getGroupName());
        group.setGroupDescription(groupCreateDto.getGroupDescription());
        group.setUserLimit(groupCreateDto.getUserLimit());
        group.setPrivate(groupCreateDto.isPrivate());
        group.setGroupPassword(groupCreateDto.getGroupPassword());

        groupRepository.save(group);
    }

    public void leaveGroup(Long groupId, Long userId) {
        GroupEntity group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("그룹을 찾을 수 없습니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("멤버를 찾을 수 없습니다."));

        if (!group.getMembers().contains(user)) {
            throw new IllegalArgumentException("그룹에 속해 있지 않습니다.");
        }

        group.getMembers().remove(user); // 그룹의 멤버 목록에서 사용자 제거
        //user.setGroup(null); // 사용자와 그룹의 연관 관계 해제
        groupRepository.save(group);
        userRepository.save(user);
    }

    public void removeMember(Long groupId, Long userId) {
        GroupEntity group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("그룹을 찾을 수 없습니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("멤버를 찾을 수 없습니다."));

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

    // 그룹 내 메이트 목록 전체 조회
    public List<UserResponseDto> findAllUsers(Long groupId) {
        Set<User> members = findById(groupId).getMembers();
        List<UserResponseDto> dtos = new ArrayList<>();
        for (User user : members) {
            dtos.add(new UserResponseDto(user.getId(), user.getNickname(), user.getProfileImg()));
        }
        return dtos;
    }

    // 그룹에 새로운 멤버 추가
    public void addNewMember(Long groupId, Long userId) {
        GroupEntity group = findById(groupId);
        User user = userService.findUserById(userId).orElseThrow(
                () -> new NoSuchElementException("User not found")
        );
        if (!group.getMembers().contains(user)) {
            user.addGroup(group);
            groupRepository.save(group);
            userRepository.save(user);
        }
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

    @Transactional(readOnly = true)
    public List<GroupEntity> findAllGroupsByUserId(Long userId) {
        return groupRepository.findAllGroupsByUserId(userId);
    }

    // 오늘 운동한 그룹원 수 1 증가
    public void increaseTodayExercisedCount(Long userId) {
        User user = userService.findUserById(userId).orElseThrow(
                () -> new NoSuchElementException("User not found")
        );
        List<GroupEntity> groups = user.getGroups();
        if (groups.isEmpty()) {
            return;
        }
        for (GroupEntity group : groups) {
            group.increaseTodayExercisedCount();
        }
    }

    // 해당 유저가 그룹의 멤버인지 여부 확인
    @Transactional(readOnly = true)
    public boolean isMemberOfGroup(Long groupId, Long userId) {
        GroupEntity group = this.findById(groupId);
        return group.getMembers().contains(userService.findUserById(userId).orElseThrow(
                () -> new NoSuchElementException("User Not Found.")
        ));
    }

    // 유저가 가입해 있지 않은 그룹의 정보 반환
    @Transactional(readOnly = true)
    public GroupInfoResponse getGroupInfoForNonMember(Long groupId) {
        GroupEntity group = this.findById(groupId);
        Page<FeedInfoResponse> feeds = exerciseService.getGroupsAllExercisePics(0, groupId);
        return new GroupInfoResponse(group.getGroupName(), group.getGroupImg(), group.isPrivate(), group.getMembers().size(),group.getUserLimit(), group.getCumulativeTime(), schoolRankingService.getRatioOfParticipate(group), feeds);
    }

    // 유저가 가입해 있는 그룹의 정보 반환
    @Transactional(readOnly = true)
    public GroupInfoResponse getGroupInfoForMember(Long groupId) {
        GroupEntity group = this.findById(groupId);
        Page<FeedInfoResponse> feeds = exerciseService.getGroupsAllExercises(0, groupId);
        return new GroupInfoResponse(group.getGroupName(), group.isPrivate(), group.getMembers().size(), group.getTodayExercisedCount(), feeds);
    }

}