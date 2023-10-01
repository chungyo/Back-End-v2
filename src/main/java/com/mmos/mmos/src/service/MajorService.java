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

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MajorService {

    private final MajorRepository majorRepository;
    private final CollegeRepository collegeRepository;

    public College findCollegeByIdx(Long collegeIdx) {
        return collegeRepository.findById(collegeIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 단과대학입니다. COLLEGE_INDEX" + collegeIdx));
    }

    public List<Major> findAllMajors(College college){
        return majorRepository.findAllByCollege(college)
                .orElse(null);
    }

    @Transactional
    public MajorResponseDto saveMajor(Long collegeIdx, MajorSaveRequestDto requestDto) {
        College college = findCollegeByIdx(collegeIdx);
        Major major = new Major(requestDto, college);
        majorRepository.save(major);
        college.addMajor(major);

        return new MajorResponseDto(major);
    }

    @Transactional
    public List<MajorResponseDto> getMajors(Long collegeIdx){
        // college 객체 가져오기
        College college = findCollegeByIdx(collegeIdx);

        // college에 소속한 major 리스트로 가져오기
        List<Major> majorList = findAllMajors(college);
        List<MajorResponseDto> majorResponseDtoList = new ArrayList<>();

        // dto 변환
        for(Major major : majorList){
            majorResponseDtoList.add(new MajorResponseDto(major));
        }

        return majorResponseDtoList;
    }
}