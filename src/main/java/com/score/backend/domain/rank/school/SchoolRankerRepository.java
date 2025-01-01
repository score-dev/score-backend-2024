package com.score.backend.domain.rank.school;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolRankerRepository extends JpaRepository<SchoolRanker, Long>, SchoolRankerRepositoryCustom {
}
