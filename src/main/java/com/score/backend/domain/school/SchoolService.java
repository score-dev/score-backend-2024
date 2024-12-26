package com.score.backend.domain.school;

import com.score.backend.domain.school.repositories.SchoolRepository;
import com.score.backend.dtos.SchoolDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SchoolService {

    private final SchoolRepository schoolRepository;

    public School findOrSave(SchoolDto schoolDto) {
        School school = findSchoolByCode(schoolDto.getSchoolCode());
        if (school == null) {
            school = saveSchool(schoolDto);
        }
        return school;
    }

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
}
