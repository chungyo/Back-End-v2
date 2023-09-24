package com.mmos.mmos.src.service;

import com.mmos.mmos.src.domain.dto.major.MajorResponseDto;
import com.mmos.mmos.src.domain.dto.major.MajorSaveRequestDto;
import com.mmos.mmos.src.domain.entity.College;
import com.mmos.mmos.src.domain.entity.Major;
import com.mmos.mmos.src.repository.CollegeRepository;
import com.mmos.mmos.src.repository.MajorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MajorService {

    private final MajorRepository majorRepository;
    private final CollegeRepository collegeRepository;

    public College findCollegeByIdx(Long collegeIdx) {
        return collegeRepository.findById(collegeIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 단과대학입니다. COLLEGE_INDEX" + collegeIdx));
    }


    @Transactional
    public MajorResponseDto saveMajor(Long collegeIdx, MajorSaveRequestDto requestDto) {
        College college = findCollegeByIdx(collegeIdx);
        Major major = new Major(requestDto, college);
        majorRepository.save(major);

        return new MajorResponseDto(major);
    }
}
