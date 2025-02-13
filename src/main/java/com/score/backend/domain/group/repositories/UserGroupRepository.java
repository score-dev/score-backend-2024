package com.score.backend.domain.group.repositories;

import com.score.backend.domain.group.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
    @Query("select ug from UserGroup ug where ug.member.id = :userId")
    List<UserGroup> findByUserId(Long userId);

    @Query("select ug from UserGroup ug where ug.member.id = :userId and ug.group.groupId = :groupId")
    UserGroup findByUserIdAndGroupId(Long userId, Long groupId);
}
