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
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 스터디입니다. STUDY_INDEX = " + studyIdx));
    }
    public User findUser(Long userIdx){
        return userRepository.findById(userIdx)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 유저입니다. STUDY_INDEX = " + userIdx));
    }

    // 해당 스터디에 가입된 유저인지 확인
    public boolean findUserIsAlreadyExist(Long userIdx, Long studyIdx){
        User user = findUser(userIdx);
        for(UserStudy userStudy : user.getUser_userstudies()) {
            if(userStudy.getStudy().getStudy_index().equals(studyIdx))
                return true;
        }
        return false;
    }

    // 새로운 스터디원 등록
    @Transactional
    public UserStudy saveUserStudy(boolean isLeader, Long studyIdx, Long userIdx) {
        Study study = findStudy(studyIdx);
        User user = findUser(userIdx);

        // 이미 가득찬 스터디인지 확인
        if(study.getStudy_userstudies().size() == study.getStudy_member_limit()){
            throw new IllegalArgumentException("이미 가득찬 스터디입니다.");
        }

        // 이미 스터디원으로 등록되어 있는지 확인
        if (!isLeader && findUserIsAlreadyExist(userIdx, studyIdx)) {
            throw new IllegalArgumentException("이미 스터디원으로 등록된 사용자입니다.");
        }
        UserStudy userStudy= new UserStudy(isLeader, user,study);
        study.addUserStudy(userStudy);
        return userStudyRepository.save(userStudy);
    }



}
