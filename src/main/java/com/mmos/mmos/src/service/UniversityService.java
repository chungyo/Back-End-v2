package com.mmos.mmos.src.service;

import com.mmos.mmos.src.domain.entity.University;
import com.mmos.mmos.src.repository.UniversityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
