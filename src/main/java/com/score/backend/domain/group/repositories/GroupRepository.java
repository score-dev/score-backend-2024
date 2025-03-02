package com.score.backend.domain.group.repositories;

import com.score.backend.domain.group.GroupEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<GroupEntity, Long>, GroupRepositoryCustom {
    // 유저가 속한 모든 그룹 목록 조회
    @Query("select g from GroupEntity g join g.members m where m.id = :userId")
    List<GroupEntity> findAllGroupsByUserId(@Param("userId")Long userId);

    // 해당 학교 소속의 모든 그룹 목록 조회
    List<GroupEntity> findByBelongingSchoolId(Long schoolId);
}
