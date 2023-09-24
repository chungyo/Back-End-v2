package com.mmos.mmos.src.service;

import com.mmos.mmos.src.domain.dto.college.CollegeResponseDto;
import com.mmos.mmos.src.domain.dto.university.UniversitySaveRequestDto;
import com.mmos.mmos.src.domain.entity.College;
import com.mmos.mmos.src.domain.entity.University;
import com.mmos.mmos.src.repository.CollegeRepository;
import com.mmos.mmos.src.repository.UniversityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CollegeService {

    private final CollegeRepository collegeRepository;
    private final UniversityRepository universityRepository;

    public University findUniversityByIdx(Long universityIdx) {
        return universityRepository.findById(universityIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 대학입니다. UNIVERSITY_INDEX" + universityIdx));
    }

    @Transactional
    public CollegeResponseDto saveCollege(Long universityIdx, UniversitySaveRequestDto requestDto) {
        University university = findUniversityByIdx(universityIdx);
        College college = new College(requestDto, university);
        collegeRepository.save(college);

        return new CollegeResponseDto(college);
    }


}
