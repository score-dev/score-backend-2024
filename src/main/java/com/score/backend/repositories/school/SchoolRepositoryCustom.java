package com.score.backend.repositories.school;

import com.score.backend.models.School;

import java.util.List;

public interface SchoolRepositoryCustom {
    School findByNameAndAddress(String code);
}
