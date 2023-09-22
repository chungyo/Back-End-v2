package com.mmos.mmos.src.service;

import com.mmos.mmos.src.domain.dto.university.UniversityResponseDto;
import com.mmos.mmos.src.domain.entity.University;
import com.mmos.mmos.src.repository.UniversityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UniversityService {
    private final UniversityRepository universityRepository;

    public University findUniversity(Long universityIdx){
        return universityRepository.findById(universityIdx)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 대학입니다. UNIVERSITY_INDEX = "+ universityIdx));
    }
    public List<University> findUniversities(){
        return universityRepository.findAll();
    }

    @Transactional
    public UniversityResponseDto getUniversity(Long universityIdx) {
        University university = findUniversity(universityIdx);

        return new UniversityResponseDto(university);
    }

    @Transactional
    public List<UniversityResponseDto> getUniversities() {
        List<University> universityList = findUniversities();
        List<UniversityResponseDto> responseDtoList = new ArrayList<>();

        for (University university : universityList) {
            responseDtoList.add(new UniversityResponseDto(university));
        }

        return responseDtoList;
    }
}
