package com.score.backend.dtos;

import com.score.backend.domain.group.GroupEntity;
import com.score.backend.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class GroupDto {
    private Long id;
    private String name;
    private String description;
    private int userLimit;
    private int currentMembers;
    private boolean isPrivate;
    private List<String> recentMembersPic;
    private int otherMembers;


    public static GroupDto fromEntity(GroupEntity group) {
        List<String> recentMembersPic = group.getMembers().stream()
                .sorted((u1, u2) -> u2.getJoinedAt().compareTo(u1.getJoinedAt()))
                .limit(3)
                .map(User::getProfileImg)
                .collect(Collectors.toList());
        int otherMembers = group.getMembers().size() - recentMembersPic.size();

        return new GroupDto(
                group.getGroupId(),
                group.getGroupName(),
                group.getGroupDescription(),
                group.getUserLimit(),
                group.getMembers().size(),
                group.isPrivate(),
                recentMembersPic,
                otherMembers
        );
    }
}
