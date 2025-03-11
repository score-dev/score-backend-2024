package com.score.backend.domain.report.userreport.repositories;

import org.springframework.stereotype.Repository;

@Repository
public interface UserReportRepositoryCustom {
    int findDistinctReportCounts(Long userId);
}
