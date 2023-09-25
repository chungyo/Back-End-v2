package com.mmos.mmos.src.service;

import com.mmos.mmos.src.domain.dto.userstudy.UserStudyInviteDto;
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

@Service
@RequiredArgsConstructor
public class UserStudyService {
    final private UserRepository userRepository;
    final private UserStudyRepository userStudyRepository;
    final private StudyRepository studyRepository;

    public Study findStudyByIdx(Long studyIdx) {
        return studyRepository.findById(studyIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스터디입니다. STUDY_INDEX = " + studyIdx));
    }

    public User findUserByIdx(Long userIdx) {
        return userRepository.findById(userIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다. STUDY_INDEX = " + userIdx));
    }

    public UserStudy findUserStudyByStudyIdxAndUserIdx(Long studyIdx, Long userIdx) {
        return userStudyRepository.findUserStudyByStudy_StudyIndexAndUser_UserIndex(studyIdx, userIdx)
                .orElse(null);
    }

    public UserStudy findUserStudyByIdx(Long userStudyIdx) {
        return userStudyRepository.findById(userStudyIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저스터디입니다. USER_STUDY_INDEX=" + userStudyIdx));
    }

    @Transactional
    public UserStudyResponseDto inviteStudy(Long userStudyIdx, UserStudyInviteDto requestDto) {
        // userStudyIdx가 리더의 것인지 확인
        UserStudy leaderUserStudy = findUserStudyByIdx(userStudyIdx);
        if(leaderUserStudy.getUserstudyMemberStatus() > 2)
            return new UserStudyResponseDto(leaderUserStudy);

        // 이미 멤버인지 혹은 보내거나 받은 요청이 있는지 체크
        UserStudy userStudy = findUserStudyByStudyIdxAndUserIdx(leaderUserStudy.getStudy().getStudyIndex(), requestDto.getUserIdx());
        if(userStudy != null) {
            if(userStudy.getUserstudyMemberStatus() <= 3)
                return new UserStudyResponseDto(userStudy);
            else
                return null;
        }

        // 객체 불러오기
        Study study = findStudyByIdx(leaderUserStudy.getStudy().getStudyIndex());
        User user = findUserByIdx(requestDto.getUserIdx());

        userStudy = new UserStudy(4, user, study);
        study.addUserStudy(userStudy);
        user.adduserStudies(userStudy);

        return new UserStudyResponseDto(userStudyRepository.save(userStudy));
    }


    @Transactional
    public UserStudyResponseDto saveUserStudy(Integer isMember, Long userIdx, Long studyIdx) {
        // 객체 불러오기
        Study study = findStudyByIdx(studyIdx);
        User user = findUserByIdx(userIdx);

        // 중복 검사
        for (UserStudy userStudy : study.getStudyUserstudies())
            if (userStudy.getUser().equals(user))
                return null;

        // 객체 생성
        UserStudy userStudy = new UserStudy(isMember, user, study);
        // 매핑
        study.addUserStudy(userStudy);
        user.adduserStudies(userStudy);

        userStudyRepository.save(userStudy);

        return new UserStudyResponseDto(userStudy);
    }

    @Transactional
    public UserStudyResponseDto acceptInvite(Long studyIdx, Long userIdx) {
        UserStudy userStudy = findUserStudyByStudyIdxAndUserIdx(studyIdx, userIdx);
        // 받은 요청이 있는지 확인 && 이미 받은 요청인지 확인
        if(userStudy == null || userStudy.getUserstudyMemberStatus() <= 3)
            return null;

        userStudy.updateMemberStatus(3);

        return new UserStudyResponseDto(userStudy);
    }

    @Transactional
    public Long rejectInvite(Long studyIdx, Long userIdx) {
        UserStudy userStudy = findUserStudyByStudyIdxAndUserIdx(studyIdx, userIdx);
        if(userStudy == null)
            return null;

        userStudyRepository.delete(userStudy);

        return userStudy.getUserstudyIndex();
    }

    public Long cancelInvite(Long userStudyIdx, UserStudyInviteDto requestDto) {
        // 취소하려는 userStudyIdx가 임원진인지 확인
        UserStudy leaderUserStudy = findUserStudyByIdx(userStudyIdx);
        if(leaderUserStudy.getUserstudyMemberStatus() > 2)
            return -1L;

        // 취소하려는 초대가 유효한지 확인
        UserStudy userStudy = findUserStudyByStudyIdxAndUserIdx(leaderUserStudy.getStudy().getStudyIndex(), requestDto.getUserIdx());
        if(userStudy == null || userStudy.getUserstudyMemberStatus() <= 3)
            return -2L;

        userStudyRepository.delete(userStudy);
        return userStudy.getUserstudyIndex();
    }


//    // 리더 위임
//    @Transactional
//    public List<UserStudyResponseDto> updateLeader(Long leaderUserIdx, Long newLeaderUserIdx) {
//
//        // 유저스터디
//        UserStudy leader = findUserStudy(leaderUserIdx);
//        UserStudy newLeader = findUserStudy(newLeaderUserIdx);
//
//        // 리더 확인
//        if(leader.getUserstudyMemberStatus()!=1){
//            System.out.println("리더가 아닙니다.");
//            return null;
//        }
//
//        // 같은 스터디인지 확인
//        if (!leader.getStudy().equals(newLeader.getStudy())) {
//            System.out.println("같은 스터디 소속이 아닙니다.");
//            return null;
//        }
//
//        // 리더 위임
//        leader.updateMemberStatus(3);
//        newLeader.updateMemberStatus(1);
//        List<UserStudyResponseDto> userStudyResponseDtoList = new ArrayList<>();
//        userStudyResponseDtoList.add(new UserStudyResponseDto(leader));
//        userStudyResponseDtoList.add(new UserStudyResponseDto(newLeader));
//
//        return userStudyResponseDtoList;
//    }
//
//    // 멤버 지위 변경
//    @Transactional
//    public UserStudyResponseDto updateMember(Long UserStudyIdx, Integer status) {
//
//        // 유저스터디
//        UserStudy userStudy = findUserStudy(UserStudyIdx);
//
//        // 현재 지위 확인
//        if (Objects.equals(userStudy.getUserstudyMemberStatus(), status)) {
//            System.out.println("이미 변경 되었습니다.");
//            return null;
//        }
//
//        // 멤버 상태 업데이트
//        userStudy.updateMemberStatus(status);
//
//        return new UserStudyResponseDto(userStudy);
//    }

}
