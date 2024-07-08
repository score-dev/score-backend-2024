package com.score.backend.repositories.school;

import com.score.backend.models.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolRepository extends JpaRepository<School, Long>, SchoolRepositoryCustom {
}
