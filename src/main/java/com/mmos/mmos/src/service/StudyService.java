package com.mmos.mmos.src.service;

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
    public Study saveStudy(StudySaveRequestDto requestDto){
        Study study = requestDto.toEntity();
        
        return studyRepository.save(study);
    }

    public Study findStudy(Long studyIdx){
        return studyRepository.findById(studyIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스터디입니다. STUDY_INDEX" + studyIdx));
    }
    @Transactional
    public void updateStudyName(Long studyIdx, String name) {
        Study study = findStudy(studyIdx);
        study.updateStudy_name(name);
    }
}
