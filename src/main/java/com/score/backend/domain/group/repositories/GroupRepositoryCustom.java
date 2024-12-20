package com.score.backend.domain.group.repositories;

import com.score.backend.domain.group.GroupEntity;

import java.util.List;

public interface GroupRepositoryCustom {
    List<GroupEntity> findGroupsByKeyword(Long schoolId, String keyword);
}
