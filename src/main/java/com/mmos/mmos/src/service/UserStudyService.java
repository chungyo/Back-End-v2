package com.mmos.mmos.src.service;

import com.mmos.mmos.src.domain.entity.Study;
import com.mmos.mmos.src.domain.entity.User;
import com.mmos.mmos.src.domain.entity.UserStudy;
import com.mmos.mmos.src.repository.StudyRepository;
import com.mmos.mmos.src.repository.UserRepository;
import com.mmos.mmos.src.repository.UserStudyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserStudyService {
    final private UserRepository userRepository;
    final private UserStudyRepository userStudyRepository;
    final private StudyRepository studyRepository;

    public Study findStudy(Long studyIdx){
        return studyRepository.findById(studyIdx)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 스터디입니다. STUDY_INDEX = " + studyIdx));
    }
    public User findUser(Long userIdx){
        return userRepository.findById(userIdx)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 유저입니다. STUDY_INDEX = " + userIdx));
    }

    @Transactional
    public UserStudy saveUserStudy(Boolean isLeader, Long studyIdx, Long userIdx) {
        // 객체 불러오기
        Study study = findStudy(studyIdx);
        User user = findUser(userIdx);
        // 객체 생성
        UserStudy userStudy = new UserStudy(isLeader, user, study);
        // 매핑
        study.addUserStudy(userStudy);
        user.adduserStudies(userStudy);


        return userStudyRepository.save(userStudy);
    }


}
