package com.mmos.mmos.src.service;

import com.mmos.mmos.config.exception.*;
import com.mmos.mmos.src.domain.entity.Study;
import com.mmos.mmos.src.domain.entity.User;
import com.mmos.mmos.src.domain.entity.UserStudy;
import com.mmos.mmos.src.repository.UserStudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.mmos.mmos.config.HttpResponseStatus.*;

@Service
@RequiredArgsConstructor
public class UserStudyService {
    private final UserStudyRepository userStudyRepository;
    private final UserService userService;
    private final StudyService studyService;

    public UserStudy findUserStudyByStudyAndUser(Study study, User user) throws BaseException {
        return userStudyRepository.findUserStudyByStudyAndUser(study, user)
                .orElseThrow(() -> new EmptyEntityException(EMPTY_USERSTUDY));
    }

    public UserStudy findUserStudyByIdx(Long userStudyIdx) throws BaseException {
        return userStudyRepository.findById(userStudyIdx)
                .orElseThrow(() -> new EmptyEntityException(EMPTY_USERSTUDY));
    }

    @Transactional
    public void updateStudyNum(UserStudy userStudy, boolean isPlus) throws BaseException {
        try {
            if(isPlus)
                userStudy.getStudy().updateMemberNum(1);
            else
                userStudy.getStudy().updateMemberNum(-1);
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
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
            if(!leaderUserStudy.getUserstudyMemberStatus().equals(1))
                throw new NotAuthorizedAccessException(USERSTUDY_INVALID_REQUEST);

            // 인원 수가 충분한지 체크
            if(leaderUserStudy.getStudy().getStudyMemberLimit() <= leaderUserStudy.getStudy().getStudyMemberNum())
                throw new OutOfRangeException(USERSTUDY_MEMBER_LIMIT_FULL);

            // 이미 멤버인지 혹은 보내거나 받은 요청이 있는지 체크
            User user = userService.findUserById(id);
            UserStudy userStudy;
            try {
                userStudy = findUserStudyByStudyAndUser(leaderUserStudy.getStudy(), user);
                throw new DuplicateRequestException(USERSTUDY_COMPLETE_REQUEST);
            } catch (EmptyEntityException e) {
                e.printStackTrace();
                // 객체 불러오기
                Study study = studyService.getStudy(leaderUserStudy.getStudy().getStudyIndex());

                userStudy = new UserStudy(3, user, study);
                study.addUserStudy(userStudy);
                user.adduserStudies(userStudy);

                return userStudyRepository.save(userStudy);
            }
        } catch (EmptyEntityException |
                 DuplicateRequestException |
                 NotAuthorizedAccessException |
                 OutOfRangeException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public UserStudy saveUserStudy(Integer memberStatus, Long userIdx, Long studyIdx) throws BaseException {
        try {
            // 객체 불러오기
            Study study = studyService.getStudy(studyIdx);
            User user = userService.getUser(userIdx);
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

            return userStudyRepository.save(userStudy);
        } catch (EmptyEntityException |
                 DuplicateRequestException |
                 OutOfRangeException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }



    @Transactional
    public void deleteUserStudy(Long adminUserStudyIdx, Long targetUserStudyIdx, boolean isAdmin) throws BaseException {
        try {
            if(isAdmin) {
                UserStudy adminUserStudy = findUserStudyByIdx(adminUserStudyIdx);
                if(!adminUserStudy.getUserstudyMemberStatus().equals(1))
                    throw new NotAuthorizedAccessException(NOT_AUTHORIZED);
            }
            UserStudy targetUserStudy = findUserStudyByIdx(targetUserStudyIdx);

            userStudyRepository.delete(targetUserStudy);

        } catch (EmptyEntityException |
                 NotAuthorizedAccessException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public UserStudy updateUserStudy(Long adminUserStudyIdx, Long targetUserStudyIdx, boolean isAdmin, Integer memberStatus) throws BaseException {
        try {
            if(isAdmin) {
                UserStudy adminUserStudy = findUserStudyByIdx(adminUserStudyIdx);
                if(!adminUserStudy.getUserstudyMemberStatus().equals(1))
                    throw new NotAuthorizedAccessException(NOT_AUTHORIZED);
            }

            UserStudy targetUserStudy = findUserStudyByIdx(targetUserStudyIdx);
            if(targetUserStudy.getStudy().getStudyMemberLimit() <= targetUserStudy.getStudy().getStudyMemberNum())
                throw new OutOfRangeException(USERSTUDY_MEMBER_LIMIT_FULL);

            if(targetUserStudy.getUserstudyMemberStatus() >= 3 &&
                    memberStatus <= 2)
                updateStudyNum(targetUserStudy, true);
            targetUserStudy.updateMemberStatus(memberStatus);

            return targetUserStudy;
        } catch (EmptyEntityException |
                 NotAuthorizedAccessException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}