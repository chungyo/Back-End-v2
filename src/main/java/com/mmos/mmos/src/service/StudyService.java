package com.mmos.mmos.src.service;

import com.mmos.mmos.src.domain.dto.study.StudyResponseDto;
import com.mmos.mmos.src.domain.dto.study.StudySaveRequestDto;
import com.mmos.mmos.src.domain.entity.Study;
import com.mmos.mmos.src.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudyService {
    private final StudyRepository studyRepository;

    // 스터디 생성
    @Transactional
    public StudyResponseDto saveStudy(StudySaveRequestDto requestDto){
        Study study = new Study(requestDto);

        // 스터디 생성
        studyRepository.save(study);
        return new StudyResponseDto(study);
    }

    public Study findStudy(Long studyIdx){
        return studyRepository.findById(studyIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스터디입니다. STUDY_INDEX" + studyIdx));
    }
    @Transactional
    public StudyResponseDto updateStudyName(Long studyIdx, String newName) {
        Study study = findStudy(studyIdx);
        
        // 이름 중복 검사
        if(study.getStudyName().equals(newName)) return null;

        study.updateStudy_name(newName);
        return new StudyResponseDto(study);
    }
}
