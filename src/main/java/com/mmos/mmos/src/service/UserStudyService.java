package com.mmos.mmos.src.service;

import com.mmos.mmos.config.exception.*;
import com.mmos.mmos.src.domain.entity.Study;
import com.mmos.mmos.src.domain.entity.User;
import com.mmos.mmos.src.domain.entity.UserStudy;
import com.mmos.mmos.src.repository.StudyRepository;
import com.mmos.mmos.src.repository.UserRepository;
import com.mmos.mmos.src.repository.UserStudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.mmos.mmos.config.HttpResponseStatus.*;

@Service
@RequiredArgsConstructor
public class UserStudyService {
    private final UserRepository userRepository;
    private final UserStudyRepository userStudyRepository;
    private final StudyRepository studyRepository;

    public Study findStudyByIdx(Long studyIdx) throws BaseException {
        return studyRepository.findById(studyIdx)
                .orElseThrow(() -> new EmptyEntityException(EMPTY_STUDY));
    }

    public User findUserByIdx(Long userIdx) throws BaseException {
        return userRepository.findById(userIdx)
                .orElseThrow(() -> new EmptyEntityException(EMPTY_USER));
    }

    public User findUserById(String id) throws BaseException {
        return userRepository.findUserByUserId(id)
                .orElseThrow(() -> new EmptyEntityException(EMPTY_USER));
    }

    public UserStudy findUserStudyByStudyIdxAndUserIdx(Study study, User user) throws BaseException {
        return userStudyRepository.findUserStudyByStudyAndUser(study, user)
                .orElseThrow(() -> new EmptyEntityException(EMPTY_USERSTUDY));
    }

    public UserStudy findUserStudyByIdx(Long userStudyIdx) throws BaseException {
        return userStudyRepository.findById(userStudyIdx)
                .orElseThrow(() -> new EmptyEntityException(EMPTY_USERSTUDY));
    }

    public List<UserStudy> findUserStudiesByStudyIdxAndStatus(Long studyIdx, Integer status) {
        return userStudyRepository.findUserStudiesByStudy_StudyIndexAndUserstudyMemberStatus(studyIdx, status)
                .orElse(null);
    }

    @Transactional
    public UserStudy getUserStudy(Long userStudyIdx) throws BaseException {
        try {
            return findUserStudyByIdx(userStudyIdx);
        } catch (EmptyEntityException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 인원 수 체크
    @Transactional
    public UserStudy inviteStudy(Long userStudyIdx, String id) throws BaseException {
        try {
            // userStudyIdx가 리더의 것인지 확인
            UserStudy leaderUserStudy = findUserStudyByIdx(userStudyIdx);
            if(leaderUserStudy.getUserstudyMemberStatus().equals(1))
                throw new NotAuthorizedAccessException(USERSTUDY_INVALID_REQUEST);

            // 인원 수가 충분한지 체크
            if(leaderUserStudy.getStudy().getStudyMemberLimit() <= leaderUserStudy.getStudy().getStudyMemberNum())
                throw new OutOfRangeException(USERSTUDY_MEMBER_LIMIT_FULL);

            // 이미 멤버인지 혹은 보내거나 받은 요청이 있는지 체크
            User user = findUserById(id);
            UserStudy userStudy;
            try {
                userStudy = findUserStudyByStudyIdxAndUserIdx(leaderUserStudy.getStudy(), user);
                throw new DuplicateRequestException(USERSTUDY_COMPLETE_REQUEST);
            } catch (BaseException e) {
                // 객체 불러오기
                Study study = findStudyByIdx(leaderUserStudy.getStudy().getStudyIndex());

                userStudy = new UserStudy(3, user, study);
                study.addUserStudy(userStudy);
                user.adduserStudies(userStudy);

                return userStudyRepository.save(userStudy);
            }
        } catch (EmptyEntityException |
                 DuplicateRequestException |
                 OutOfRangeException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public UserStudy saveUserStudy(Integer memberStatus, Long userIdx, Long studyIdx) throws BaseException {
        try {
            // 객체 불러오기
            Study study = findStudyByIdx(studyIdx);
            User user = findUserByIdx(userIdx);

            // 인원 수가 충분한지 체크
            if(study.getStudyMemberLimit() <= study.getStudyMemberNum()) {
                throw new OutOfRangeException(USERSTUDY_MEMBER_LIMIT_FULL);
            }

            // 중복 검사
            for (UserStudy userStudy : study.getStudyUserstudies())
                if (userStudy.getUser().equals(user)) {
                    throw new DuplicateRequestException(USERSTUDY_COMPLETE_REQUEST);
                }

            // 객체 생성
            UserStudy userStudy = new UserStudy(memberStatus, user, study);
            // 매핑
            study.addUserStudy(userStudy);
            user.adduserStudies(userStudy);
            study.plusMemberNum();

            return userStudyRepository.save(userStudy);
        } catch (EmptyEntityException |
                 DuplicateRequestException |
                 OutOfRangeException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public void deleteUserStudy(Long adminUserStudyIdx, Long targetUserStudyIdx, boolean isAdmin) throws BaseException {
        try {
            if(isAdmin) {
                UserStudy adminUserStudy = findUserStudyByIdx(adminUserStudyIdx);
                if(adminUserStudy.getUserstudyMemberStatus().equals(1))
                    throw new NotAuthorizedAccessException(NOT_AUTHORIZED);
            }

            UserStudy targetUserStudy = findUserStudyByIdx(targetUserStudyIdx);
            userStudyRepository.delete(targetUserStudy);

        } catch (EmptyEntityException |
                 NotAuthorizedAccessException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public UserStudy updateUserStudy(Long adminUserStudyIdx, Long targetUserStudyIdx, boolean isAdmin, Integer memberStatus) throws BaseException {
        try {
            if(isAdmin) {
                UserStudy adminUserStudy = findUserStudyByIdx(adminUserStudyIdx);
                if(adminUserStudy.getUserstudyMemberStatus().equals(1))
                    throw new NotAuthorizedAccessException(NOT_AUTHORIZED);
            }

            UserStudy targetUserStudy = findUserStudyByIdx(targetUserStudyIdx);
            if(targetUserStudy.getStudy().getStudyMemberLimit() <= targetUserStudy.getStudy().getStudyMemberNum())
                throw new OutOfRangeException(USERSTUDY_MEMBER_LIMIT_FULL);

            if(targetUserStudy.getUserstudyMemberStatus() >= 3 &&
                    memberStatus <= 2)
                targetUserStudy.getStudy().plusMemberNum();
            targetUserStudy.updateMemberStatus(memberStatus);

            return targetUserStudy;
        } catch (EmptyEntityException |
                 NotAuthorizedAccessException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


}