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

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CollegeService {

    private final CollegeRepository collegeRepository;
    private final UniversityRepository universityRepository;

    public University findUniversityByIdx(Long universityIdx) {
        return universityRepository.findById(universityIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 대학입니다. UNIVERSITY_INDEX" + universityIdx));
    }

    public List<College> findAllColleges(University university) {
        return collegeRepository.findAllByUniversity(university)
                .orElse(null);
    }

    @Transactional
    public CollegeResponseDto saveCollege(Long universityIdx, UniversitySaveRequestDto requestDto) {
        University university = findUniversityByIdx(universityIdx);
        College college = new College(requestDto, university);
        collegeRepository.save(college);
        university.addCollege(college);

        return new CollegeResponseDto(college);
    }

    @Transactional
    public List<CollegeResponseDto> getColleges(Long universityIdx) {
        // university 객체 가져오기
        University university = findUniversityByIdx(universityIdx);

        // university에 소속한 college 리스트로 가져오기
        List<College> collegeList = findAllColleges(university);
        List<CollegeResponseDto> collegeResponseDtoList = new ArrayList<>();

        // dto 변환
        for (College college : collegeList) {
            collegeResponseDtoList.add(new CollegeResponseDto(college));
        }

        return collegeResponseDtoList;
    }
}