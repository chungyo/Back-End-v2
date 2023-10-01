package com.mmos.mmos.src.service;

import com.mmos.mmos.src.domain.dto.userstudy.UserStudyAttendDto;
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

import java.util.List;

import static com.mmos.mmos.config.HttpResponseStatus.*;

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

    public List<UserStudy> findUserStudiesByStudyIdxAndStatus(Long studyIdx, Integer status) {
        return userStudyRepository.findUserStudiesByStudy_StudyIndexAndUserstudyMemberStatus(studyIdx, status)
                .orElse(null);
    }

    // 인원 수 체크
    @Transactional
    public UserStudyResponseDto inviteStudy(Long userStudyIdx, UserStudyInviteDto requestDto) {
        // userStudyIdx가 리더의 것인지 확인
        UserStudy leaderUserStudy = findUserStudyByIdx(userStudyIdx);
        if(leaderUserStudy.getUserstudyMemberStatus() > 2) {
            return new UserStudyResponseDto(USERSTUDY_INVALID_REQUEST);
        }

        // 인원 수가 충분한지 체크
        if(leaderUserStudy.getStudy().getStudyMemberLimit() <= leaderUserStudy.getStudy().getStudyMemberNum()) {
            return new UserStudyResponseDto(USERSTUDY_MEMBER_LIMIT_FULL);

        }

        // 이미 멤버인지 혹은 보내거나 받은 요청이 있는지 체크
        UserStudy userStudy = findUserStudyByStudyIdxAndUserIdx(leaderUserStudy.getStudy().getStudyIndex(), requestDto.getUserIdx());
        if(userStudy != null) {
            if(userStudy.getUserstudyMemberStatus() <= 3) {
                return new UserStudyResponseDto(USERSTUDY_MEMBER_ALREADY_EXIST);
            }
            else {
                return new UserStudyResponseDto(USERSTUDY_COMPLETE_REQUEST);
            }
        }

        // 객체 불러오기
        Study study = findStudyByIdx(leaderUserStudy.getStudy().getStudyIndex());
        User user = findUserByIdx(requestDto.getUserIdx());

        userStudy = new UserStudy(4, user, study);
        study.addUserStudy(userStudy);
        user.adduserStudies(userStudy);

        return new UserStudyResponseDto(userStudyRepository.save(userStudy), SUCCESS);
    }


    @Transactional
    public UserStudyResponseDto saveUserStudy(Integer isMember, Long userIdx, Long studyIdx) {
        // 객체 불러오기
        Study study = findStudyByIdx(studyIdx);
        User user = findUserByIdx(userIdx);

        // 인원 수가 충분한지 체크
        if(study.getStudyMemberLimit() <= study.getStudyMemberNum())
            return null;

        // 중복 검사
        for (UserStudy userStudy : study.getStudyUserstudies())
            if (userStudy.getUser().equals(user))
                return null;

        // 객체 생성
        UserStudy userStudy = new UserStudy(isMember, user, study);
        // 매핑
        study.addUserStudy(userStudy);
        user.adduserStudies(userStudy);
        study.plusMemberNum();

        userStudyRepository.save(userStudy);

        return new UserStudyResponseDto(userStudy, SUCCESS);
    }

    @Transactional
    public UserStudyResponseDto acceptInvite(Long studyIdx, Long userIdx) {
        UserStudy userStudy = findUserStudyByStudyIdxAndUserIdx(studyIdx, userIdx);

        // 받은 요청이 있는지 확인 && 이미 받은 요청인지 확인
        if(userStudy == null || userStudy.getUserstudyMemberStatus() <= 3) {
            return new UserStudyResponseDto(USERSTUDY_COMPLETE_REQUEST);
        }

        // 스터디원이 가득 찼는지 확인
        // 인원 수가 충분한지 체크
        if(userStudy.getStudy().getStudyMemberLimit() <= userStudy.getStudy().getStudyMemberNum()) {
            return new UserStudyResponseDto(USERSTUDY_MEMBER_LIMIT_FULL);
        }

        userStudy.updateMemberStatus(3);
        userStudy.getStudy().plusMemberNum();


        return new UserStudyResponseDto(userStudy, SUCCESS);
    }

    @Transactional
    public Long rejectInvite(Long studyIdx, Long userIdx) {
        UserStudy userStudy = findUserStudyByStudyIdxAndUserIdx(studyIdx, userIdx);
        if(userStudy == null) {
            return null;
        }

        userStudyRepository.delete(userStudy);

        return userStudy.getUserstudyIndex();
    }

    @Transactional
    public Long cancelInvite(Long userStudyIdx, UserStudyInviteDto requestDto) {
        // 취소하려는 userStudyIdx가 임원진인지 확인
        UserStudy leaderUserStudy = findUserStudyByIdx(userStudyIdx);
        if(leaderUserStudy.getUserstudyMemberStatus() > 2) {
            return -1L;
        }

        // 취소하려는 초대가 유효한지 확인
        UserStudy userStudy = findUserStudyByStudyIdxAndUserIdx(leaderUserStudy.getStudy().getStudyIndex(), requestDto.getUserIdx());
        if(userStudy == null || userStudy.getUserstudyMemberStatus() <= 3) {
            return -2L;
        }

        userStudyRepository.delete(userStudy);
        return userStudy.getUserstudyIndex();
    }

    @Transactional
    public UserStudyResponseDto attendRequest(Long userIdx, UserStudyAttendDto requestDto) {
        // 이미 참여중이거나 보낸 요청이 있는지 확인
        UserStudy userStudy = findUserStudyByStudyIdxAndUserIdx(requestDto.getStudyIdx(), userIdx);
        if(userStudy != null) {
            if(userStudy.getUserstudyMemberStatus() <= 3) {
                return new UserStudyResponseDto(USERSTUDY_ALREADY_EXIST);
            }
            else {
                return new UserStudyResponseDto(USERSTUDY_COMPLETE_REQUEST);
            }
        }

        // 객체 가져오기
        User user = findUserByIdx(userIdx);
        Study study = findStudyByIdx(requestDto.getStudyIdx());

        // 인원 수 체크
        if(study.getStudyMemberLimit() <= study.getStudyMemberNum()) {
            return new UserStudyResponseDto(USERSTUDY_ALREADY_EXIST);
        }

        // 저장
        userStudy = new UserStudy(5, user, study);
        user.adduserStudies(userStudy);
        study.addUserStudy(userStudy);

        return new UserStudyResponseDto(userStudyRepository.save(userStudy), SUCCESS);
    }

    @Transactional
    public UserStudyResponseDto acceptAttend(Long userStudyIdx1, Long userStudyIdx2) {
        UserStudy leaderUserStudy = findUserStudyByIdx(userStudyIdx1);
        // 운영진인지 확인
        if(leaderUserStudy.getUserstudyMemberStatus() > 2) {
            return new UserStudyResponseDto(USERSTUDY_INVALID_REQUEST);
        }


        // 스터디원이 가득 찼는지 확인
        if(leaderUserStudy.getStudy().getStudyMemberLimit() <= leaderUserStudy.getStudy().getStudyMemberNum()) {
            return new UserStudyResponseDto(USERSTUDY_MEMBER_LIMIT_FULL);
        }

        UserStudy userStudy = findUserStudyByIdx(userStudyIdx2);

        // 이미 활동 중인 스터디거나 요청이 완료된 상황인지 확인
        if(userStudy == null || userStudy.getUserstudyMemberStatus() <= 3) {
            return new UserStudyResponseDto(USERSTUDY_COMPLETE_REQUEST);
        }

        userStudy.updateMemberStatus(3);

        if(userStudy.getStudy().getStudyUserstudies().size() == userStudy.getStudy().getStudyMemberLimit()) {
            System.out.println(userStudy.getStudy().getStudyUserstudies().size());
            userStudy.getStudy().plusMemberNum();
        }
        return new UserStudyResponseDto(userStudy, SUCCESS);
    }

    @Transactional
    public Long rejectAttend(Long userStudyIdx1, Long userStudyIdx2) {
        // 운영진인지 확인
        UserStudy leaderUserStudy = findUserStudyByIdx(userStudyIdx1);
        if(leaderUserStudy.getUserstudyMemberStatus() > 2) {
            return -1L;
        }

        // 유효한 신청인지 확인
        UserStudy userStudy = findUserStudyByIdx(userStudyIdx2);
        if(userStudy == null || userStudy.getUserstudyMemberStatus() <= 3) {
            return -2L;
        }

        userStudyRepository.delete(userStudy);

        return userStudy.getUserstudyIndex();
    }

    @Transactional
    public Long cancelAttend(Long userStudyIdx) {
        // 처리된 요청인지 아닌지만 확인하면 끝
        UserStudy userStudy = findUserStudyByIdx(userStudyIdx);
        if(userStudy == null || userStudy.getUserstudyMemberStatus() <= 3)
            return null;

        userStudyRepository.delete(userStudy);

        return userStudyIdx;
    }

    @Transactional
    public Long leaveStudy(Long userStudyIdx) {
        // 처리된 요청인지 아닌지만 확인하면 끝
        UserStudy userStudy = findUserStudyByIdx(userStudyIdx);
        if(userStudy == null || userStudy.getUserstudyMemberStatus() >= 4 || userStudy.getUserstudyMemberStatus() == 1)
            return null;

        userStudy.getStudy().minusMemberNum();
        userStudyRepository.delete(userStudy);

        return userStudyIdx;
    }

    @Transactional
    public Long kickMember(Long userStudyIdx1, Long userStudyIdx2) {
        // 운영진인지 확인
        UserStudy leaderUserStudy = findUserStudyByIdx(userStudyIdx1);
        if(leaderUserStudy.getUserstudyMemberStatus() != 1) {
            return -1L;
        }

        // 유효한 신청인지 확인
        UserStudy userStudy = findUserStudyByIdx(userStudyIdx2);
        if(userStudy == null || userStudy.getUserstudyMemberStatus() >= 4 || userStudy.getUserstudyMemberStatus() == 1) {
            return -2L;
        }

        userStudy.getStudy().minusMemberNum();
        userStudyRepository.delete(userStudy);

        return userStudy.getUserstudyIndex();
    }

    @Transactional
    public UserStudyResponseDto updatePosition(Long userStudyIdx1, Long userStudyIdx2, Long position) {
        // 운영진인지 확인
        UserStudy manager = findUserStudyByIdx(userStudyIdx1);
        if(manager == null || manager.getUserstudyMemberStatus() > 2) {
            return new UserStudyResponseDto(USERSTUDY_INVALID_REQUEST);
        }
        UserStudy userStudy = findUserStudyByIdx(userStudyIdx2);

        // 멤버 -> 운영진: 운영진, 스터디 장 모두 가능한 권한
        if(userStudy.getUserstudyMemberStatus() == 3 && position == 2)
            userStudy.updateMemberStatus(position.intValue());
        // 그 외 스터디 장만이 가능한 권한
        if(manager.getUserstudyMemberStatus() == 1 && userStudy.getUserstudyMemberStatus() != 1) {
            userStudy.updateMemberStatus(position.intValue());
            // 스터디 장 위임
            if(position == 1) {
                manager.updateMemberStatus(2);
            }
        }

        return new UserStudyResponseDto(userStudy, SUCCESS);
    }



}