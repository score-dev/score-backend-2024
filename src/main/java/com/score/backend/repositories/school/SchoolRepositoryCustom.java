package com.score.backend.repositories.school;

import com.score.backend.models.School;

public interface SchoolRepositoryCustom {
    School findSchoolByCode(String code);
}
