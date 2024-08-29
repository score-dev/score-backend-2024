package com.score.backend.repositories.group;

import com.score.backend.models.GroupEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<GroupEntity, Long>{
    // 이거 주석 해제하면 오류 나서 일단 주석 처리해뒀습니다.
//    List<Group> searchGroupFromSchool(School school, String groupName);

    // 유저가 속한 모든 그룹 목록 조회
    @Query("select g from GroupEntity g join g.members m where m.id = :userId")
    List<GroupEntity> findAllGroupsByUserId(@Param("userId")Long userId);

}
