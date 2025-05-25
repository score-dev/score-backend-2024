package com.score.backend.dtos;

import com.score.backend.domain.group.GroupEntity;
import com.score.backend.domain.group.UserGroup;
import com.score.backend.domain.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Schema(description = "그룹 정보를 응답하기 위한 DTO입니다.")
public class GroupDto {
    @Schema(description = "그룹의 id 값")
    private Long id;
    @Schema(description = "그룹명")
    private String name;
    @Schema(description = "그룹 설명")
    private String description;
    @Schema(description = "그룹 이미지 url")
    private String groupImg;
    @Schema(description = "그룹에 가입 가능한 최대 멤버 수")
    private int userLimit;
    @Schema(description = "현재 가입되어 있는 멤버 수")
    private int currentMembers;
    @Schema(description = "비공개 그룹인지 여부")
    private boolean isPrivate;
    @Schema(description = "최근 운동한 유저들의 프로필 이미지 url")
    private List<String> recentMembersPic;
    @Schema(description = "최근 운동한 유저 3명을 제외한 나머지 멤버들의 수")
    private int otherMembers;

    public static GroupDto fromEntity(GroupEntity group) {
        List<User> recentMembers = group.getMembers().stream()
                .sorted((u1, u2) -> u2.getMember().getCreatedAt().compareTo(u1.getMember().getCreatedAt()))
                .limit(3)
                .map(UserGroup::getMember)
                .toList();
        List<String> recentMembersPic = recentMembers.stream().map(User::getProfileImg).collect(Collectors.toList());
        int otherMembers = group.getMembers().size() - recentMembersPic.size();

        return new GroupDto(
                group.getGroupId(),
                group.getGroupName(),
                group.getGroupDescription(),
                group.getGroupImg(),
                group.getUserLimit(),
                group.getMembers().size(),
                group.isPrivate(),
                recentMembersPic,
                otherMembers
        );
    }
}
