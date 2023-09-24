package com.mmos.mmos.src.service;

import com.mmos.mmos.src.domain.dto.userstudy.UserStudyResponseDto;
import com.mmos.mmos.src.domain.entity.Study;
import com.mmos.mmos.src.domain.entity.User;
import com.mmos.mmos.src.domain.entity.UserStudy;
import com.mmos.mmos.src.repository.StudyRepository;
import com.mmos.mmos.src.repository.UserRepository;
import com.mmos.mmos.src.repository.UserStudyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

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

    public UserStudy findUserStudy(Long userStudyIdx){
        return userStudyRepository.findById(userStudyIdx)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 유저스터디입니다. USERSTUDY_INDEX = " + userStudyIdx));
    }

    @Transactional
    public UserStudyResponseDto saveUserStudy(Integer isMember, Long userIdx, Long studyIdx) {
        // 객체 불러오기
        Study study = findStudy(studyIdx);
        User user = findUser(userIdx);
        // 객체 생성
        UserStudy userStudy = new UserStudy(isMember, user, study);
        // 매핑
        study.addUserStudy(userStudy);
        user.adduserStudies(userStudy);

        userStudyRepository.save(userStudy);

        return new UserStudyResponseDto(userStudy);
    }

    
    // 리더 위임
    @Transactional
    public UserStudyResponseDto updateLeader(Long leaderUserIdx, Long newLeaderUserIdx){

        // 유저스터디
        UserStudy leader = findUserStudy(leaderUserIdx);
        UserStudy newLeader = findUserStudy(newLeaderUserIdx);

        // 같은 스터디인지 확인
        if(!leader.getStudy().equals(newLeader.getStudy())) {
            System.out.println("같은 스터디 소속이 아닙니다.");
            return null;
        }

        // 리더 위임
        leader.updateMemberStatus(3);
        newLeader.updateMemberStatus(1);

        return new UserStudyResponseDto(newLeader);
    }

    // 멤버 지위 변경
    @Transactional
    public UserStudyResponseDto updateMember(Long UserStudyIdx, Integer status){

        // 유저스터디
        UserStudy userStudy = findUserStudy(UserStudyIdx);

        // 현재 지위 확인
        if(Objects.equals(userStudy.getUserstudyMemberStatus(), status)) {
            System.out.println("이미 변경 되었습니다.");
            return null;
        }

        // 멤버 상태 업데이트
        userStudy.updateMemberStatus(status);

        return new UserStudyResponseDto(userStudy);
    }
}
