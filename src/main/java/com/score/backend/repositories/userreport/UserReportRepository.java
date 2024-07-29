package com.score.backend.repositories.userreport;

import com.score.backend.models.UserReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserReportRepository extends JpaRepository<UserReport, Long> , UserReportRepositoryCustom {
}
