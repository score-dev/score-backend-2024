package com.score.backend.repositories;

import com.score.backend.models.FeedReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedReportRepository extends JpaRepository<FeedReport, Long> {
}
