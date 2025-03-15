package com.score.backend.domain.group.repositories;

import com.score.backend.domain.group.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<GroupEntity, Long>, GroupRepositoryCustom {
    // 해당 학교 소속의 모든 그룹 목록 조회
    List<GroupEntity> findByBelongingSchoolId(Long schoolId);
}
