package com.score.backend.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GroupCreateDto {
    @NotNull
    private String groupImg;

    @NotNull(message = "우리 그룹 이름은 무엇으로 해볼까요?")
    @Size(min = 1, max = 15)
    private String groupName;

    @NotNull(message = "우리 그룹을 자유롭게 소개해보세요!")
    @Size(min = 1, max = 15)
    private String groupDescription;

    @NotNull
    private int userLimit;

    @NotNull
    private boolean isPrivate;

    //    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String groupPassword;

    public boolean isValid(){
        if (isPrivate && (groupPassword == null || groupPassword.isEmpty())){
            throw new IllegalArgumentException("그룹 비밀번호 4자리를 입력해주세요.");
        }
        return true;
    }
}
