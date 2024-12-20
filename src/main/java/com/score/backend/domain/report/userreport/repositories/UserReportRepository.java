package com.score.backend.domain.report.userreport.repositories;

import com.score.backend.domain.report.userreport.UserReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserReportRepository extends JpaRepository<UserReport, Long> , UserReportRepositoryCustom {
}
