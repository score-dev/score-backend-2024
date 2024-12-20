package com.score.backend.domain.school.repositories;

import com.score.backend.domain.school.School;

public interface SchoolRepositoryCustom {
    School findSchoolByCode(String code);
}
