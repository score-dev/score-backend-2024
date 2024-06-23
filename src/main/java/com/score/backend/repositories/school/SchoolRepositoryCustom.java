package com.score.backend.repositories.school;

import com.score.backend.models.School;

import java.util.List;

public interface SchoolRepositoryCustom {
    List<School> findByNameAndAddress(String name, String address);
}
