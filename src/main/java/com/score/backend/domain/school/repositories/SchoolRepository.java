package com.score.backend.domain.school.repositories;

import com.score.backend.domain.school.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolRepository extends JpaRepository<School, Long> {
    School findSchoolBySchoolCode(String schoolCode);
}
