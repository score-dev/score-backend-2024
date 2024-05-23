package com.score.backend.models.dtos;

import com.score.backend.models.Group;
import lombok.AllArgsConstructor;
import lombok.Getter;

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

    public static GroupDto fromEntity(Group group) {
        return new GroupDto(
                group.getGroupId(),
                group.getGroupImg(),
                group.getGroupName(),
                group.getGroupDescription(),
                group.getUserLimit(),
                group.getMembers().size(),
                group.isPrivate()
        );
    }
}
