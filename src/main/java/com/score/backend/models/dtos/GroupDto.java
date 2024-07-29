package com.score.backend.models.dtos;

import com.score.backend.models.Group;
import com.score.backend.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;


@Getter
@AllArgsConstructor
public class GroupDto {
    private Long id;
    private String img;
    private String name;
    private String description;
    private int userLimit;
    private int currentMembers;
    private boolean isPrivate;
    private List<String> recentMembersPic;
    private int otherMembers;


    public static GroupDto fromEntity(Group group) {
        List<String> recentMembersPic = group.getMembers().stream()
                .sorted((u1, u2) -> u2.getJoinedAt().compareTo(u1.getJoinedAt()))
                .limit(3)
                .map(User::getProfileImg)
                .collect(Collectors.toList());
        int otherMembers = group.getMembers().size() - recentMembersPic.size();

        return new GroupDto(
                group.getGroupId(),
                group.getGroupImg(),
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
