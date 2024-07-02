package com.score.backend.services;

import com.score.backend.models.School;
import com.score.backend.models.dtos.SchoolDto;
import com.score.backend.repositories.school.SchoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SchoolService {

    private final SchoolRepository schoolRepository;

    // 새로운 학교 정보 db에 저장
    public School saveSchool(SchoolDto schoolDto) {
        School school = schoolDto.toEntity();
        schoolRepository.save(school);
        return school;
    }

    // 행정 표준 코드로 학교 검색
    public School findSchoolByCode(String code) {
        return schoolRepository.findSchoolByCode(code);
    }

    // 학교명으로 학교 검색

}
