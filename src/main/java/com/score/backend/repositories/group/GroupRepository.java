package com.score.backend.repositories.group;

import com.score.backend.models.Group;
import com.score.backend.models.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long>{
    // 이거 주석 해제하면 오류 나서 일단 주석 처리해뒀습니다.
//    List<Group> searchGroupFromSchool(School school, String groupName);

}
