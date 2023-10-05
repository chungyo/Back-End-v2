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

import static com.mmos.mmos.config.HttpResponseStatus.GET_COLLEGE_EMPTY_RETURN;
import static com.mmos.mmos.config.HttpResponseStatus.SUCCESS;

@Service
@RequiredArgsConstructor
public class MajorService {

    private final MajorRepository majorRepository;
    private final CollegeRepository collegeRepository;

    public College findCollegeByIdx(Long collegeIdx) {
        return collegeRepository.findById(collegeIdx)
                .orElseThrow(null);
    }

    public List<Major> findAllMajors(College college){
        return majorRepository.findAllByCollege(college)
                .orElse(null);
    }

    @Transactional
    public MajorResponseDto saveMajor(Long collegeIdx, MajorSaveRequestDto requestDto) {
        College college = findCollegeByIdx(collegeIdx);
        if(college == null)
            return new MajorResponseDto(GET_COLLEGE_EMPTY_RETURN);
        Major major = new Major(requestDto, college);
        majorRepository.save(major);
        college.addMajor(major);

        return new MajorResponseDto(major, SUCCESS);
    }

    @Transactional
    public List<MajorResponseDto> getMajors(Long collegeIdx){
        // college 객체 가져오기
        College college = findCollegeByIdx(collegeIdx);
        if(college == null)
            return null;

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