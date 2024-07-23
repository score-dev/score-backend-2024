package com.score.backend.repositories.group;

import com.score.backend.models.Group;
import com.score.backend.models.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long>{
    List<Group> searchGroupFromSchool(School school, String groupName);

}
