package com.score.backend.services;

import com.score.backend.models.dtos.GroupDto;
import com.score.backend.models.Group;
import com.score.backend.models.User;
import com.score.backend.repositories.group.GroupRepository;
import com.score.backend.repositories.user.UserRepository;
import com.score.backend.models.dtos.GroupCreateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService{

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;


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


    public void updateGroup(Long groupId, GroupCreateDto groupCreateDto) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("그룹을 찾을 수 없습니다."));

        group.setGroupImg(groupCreateDto.getGroupImg());
        group.setGroupName(groupCreateDto.getGroupName());
        group.setGroupDescription(groupCreateDto.getGroupDescription());
        group.setUserLimit(groupCreateDto.getUserLimit());
        group.setPrivate(groupCreateDto.isPrivate());
        group.setGroupPassword(groupCreateDto.getGroupPassword());

        groupRepository.save(group);
    }

    public void removeMember(Long groupId, Long userId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("그룹을 찾을 수 없습니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("멤버를 찾을 수 없습니다."));

        if (!group.getMembers().contains(user)) {
            throw new IllegalArgumentException("그룹에 속해 있지 않습니다.");
        }

        group.getMembers().remove(user); // 그룹의 멤버 목록에서 사용자 제거
        user.setGroup(null); // 사용자와 그룹의 연관 관계 해제
        groupRepository.save(group);
        userRepository.save(user);
    }
}
