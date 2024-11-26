package com.score.backend.repositories.group;

import com.score.backend.models.GroupEntity;

import java.util.List;

public interface GroupRepositoryCustom {
    List<GroupEntity> findGroupsByKeyword(Long schoolId, String keyword);
}
