package com.score.backend.services;

import com.score.backend.models.dtos.GroupDto;
import com.score.backend.models.Group;
import com.score.backend.models.User;
import com.score.backend.repositories.group.GroupRepository;
import com.score.backend.repositories.user.UserRepository;
import com.score.backend.models.dtos.GroupCreateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class GroupService{

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final ExerciseService exerciseService;

    @Transactional(readOnly = true)
    public Group findById(Long id){
        return groupRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Group not found")
        );
    }

    public List<Group> findAll() {
        return groupRepository.findAll();
    }


    public void createGroup(GroupCreateDto groupCreateDto, Long adminId) {

        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("그룹장을 못 찾습니다."));

        Group group = Group.builder()
                .groupImg(groupCreateDto.getGroupImg())
                .groupName(groupCreateDto.getGroupName())
                .groupDescription(groupCreateDto.getGroupDescription())
                .userLimit(groupCreateDto.getUserLimit())
                .isPrivate(groupCreateDto.isPrivate())
                .groupPassword(groupCreateDto.getGroupPassword())
                .admin(admin)
                .build();

        groupRepository.save(group);
    }

    public List<GroupDto> getAllGroups() {
        List<Group> groups = groupRepository.findAll();
        return groups.stream().map(GroupDto::fromEntity).collect(Collectors.toList());
    }

    public void updateGroup(Long groupId, GroupCreateDto groupCreateDto, Long adminId) {
        Group group = groupRepository.findById(groupId)
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
        Group group = groupRepository.findById(groupId)
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
        Group group = groupRepository.findById(groupId)
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

    @Transactional(readOnly = true)
    public List<Group> findAllGroupsByUserId(Long userId) {
        return groupRepository.findAllGroupsByUserId(userId);
    }
}
