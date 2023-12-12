package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.config.exception.BaseException;
import com.mmos.mmos.config.exception.BusinessLogicException;
import com.mmos.mmos.config.exception.EmptyInputException;
import com.mmos.mmos.config.exception.NotAuthorizedAccessException;
import com.mmos.mmos.src.domain.dto.request.*;
import com.mmos.mmos.src.domain.dto.response.study.*;
import com.mmos.mmos.src.domain.entity.*;
import com.mmos.mmos.src.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.mmos.mmos.config.HttpResponseStatus.*;

@RestController
@RequestMapping("/api/v1/study")
@RequiredArgsConstructor
public class StudyPageController extends BaseController {

    private final UserService userService;
    private final StudyService studyService;
    private final UserStudyService userStudyService;
    private final PostService postService;
    private final ProjectService projectService;

    // 현재 스터디 기준 가져오기 (전체 정보가 아님) -> 스터디도 페이지로 가져오나? -> 일단 페이지로 가져오자
    @GetMapping("")
    public ResponseEntity<ResponseApiMessage> getPage(@AuthenticationPrincipal User tokenUser,
                                                        @PageableDefault(size = 1, sort = "studyIdx") Pageable studyPage,
                                                      @PageableDefault(size = 1, sort = "projectNumber", direction = Sort.Direction.ASC) Pageable projectPage,
                                                      @PageableDefault(size = 3, sort = "postIndex", direction = Sort.Direction.DESC) Pageable noticePage,
                                                      @PageableDefault(size = 5, sort = "postIndex", direction = Sort.Direction.DESC) Pageable promotionPage) {
        try {
            // 나
            User user = userService.getUser(tokenUser.getUserIndex());
            // 내 스터디들
            List<MyStudySectionDto> myStudySectionDto = new ArrayList<>();
            List<UserStudy> userStudies = user.getUserUserstudies();
            List<ProjectSectionDto> projectSectionDto = new ArrayList<>();
            List<MemberSectionInProjectDto> memberSectionInProjectDto = new ArrayList<>(); // 내꺼 포함 다른 멤버들의 프로젝트
            List<MemberSectionInMemberDto> memberSectionInMemberDto = new ArrayList<>();
            List<NoticeSectionDto> noticeSectionDto = new ArrayList<>();

            for (UserStudy userStudy : userStudies) {   // 내 스터디들 iter
                // Page<ProjectSectionDto> projectSection
                for (Project userProject : user.getUserProjects()) {    // 내 프로젝트 iter
                    if(userProject.getStudy().equals(userStudy.getStudy())) {   // 현재 스터디와 일치하면
                        for (Project studyProject : userStudy.getStudy().getStudyProjects()) {  // 다른 인원들의 프로젝트들도 추가
                            if(studyProject.getProjectNumber().equals(userProject.getProjectNumber()))
                                memberSectionInProjectDto.add(new MemberSectionInProjectDto(studyProject.getUser(), studyProject));
                        }
                        projectSectionDto.add(new ProjectSectionDto(userProject, userStudy.getStudy(), memberSectionInProjectDto));
                    }
                }

                // StudyMemberSectionDto memberSection
                for (UserStudy studyUserstudy : userStudy.getStudy().getStudyUserstudies()) {
                    memberSectionInMemberDto.add(new MemberSectionInMemberDto(studyUserstudy));
                }

                for (Post studyPost : userStudy.getStudy().getStudyPosts()) {
                    if(studyPost.getPostIsNotice())
                        noticeSectionDto.add(new NoticeSectionDto(studyPost));
                }

                myStudySectionDto.add(new MyStudySectionDto(userStudy.getStudy(),
                        new PageImpl<>(projectSectionDto, projectPage, projectSectionDto.size()),
                        new StudyMemberSectionDto(userStudy.getStudy(), memberSectionInMemberDto),
                        postService.getNotices(userStudy, noticePage)));
            }

            return sendResponseHttpByJson(SUCCESS, "스터디 페이지 로드 성공",
                    new StudyPageResponseDto(new PageImpl<>(myStudySectionDto, studyPage, myStudySectionDto.size()),
                                            postService.getPromotions(promotionPage)));
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 스터디 생성
    @PostMapping("")
    public ResponseEntity<ResponseApiMessage> saveStudy(@AuthenticationPrincipal User tokenUser, @RequestBody StudySaveRequestDto requestDto) {
        try {
            if(requestDto.getName().isEmpty())
                throw new EmptyInputException(EMPTY_STUDY_NAME);
            if(requestDto.getMemberLimit() == null)
                throw new EmptyInputException(EMPTY_STUDY_LIMIT);

            Study study = studyService.saveStudy(requestDto);
            UserStudy userStudy = userStudyService.saveUserStudy(1, tokenUser.getUserIndex(), study.getStudyIndex());
            userStudyService.updateStudyNum(userStudy, true);
            return sendResponseHttpByJson(SUCCESS, "스터디 생성 성공", study);
        } catch (BaseException e) {
            e.printStackTrace();
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 스터디 설정
    @PatchMapping("/{userStudyIdx}")
    public ResponseEntity<ResponseApiMessage> updateStudy(@PathVariable Long userStudyIdx, @RequestBody StudyUpdateRequestDto requestDto) {
        try {
            UserStudy userStudy = userStudyService.getUserStudy(userStudyIdx);
            return sendResponseHttpByJson(SUCCESS, "스터디 수정 성공",
                    studyService.updateStudy(userStudy.getStudy().getStudyIndex(), requestDto));
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 스터디 홍보 글쓰기
    @PostMapping("/promotion/{userStudyIdx}")
    public ResponseEntity<ResponseApiMessage> savePromotion(@PathVariable Long userStudyIdx, @RequestBody PostSaveRequestDto requestDto) {
        try {
            if(requestDto.getPostTitle().isEmpty())
                throw new EmptyInputException(POST_POST_EMPTY_TITLE);
            if(requestDto.getPostContents().isEmpty())
                throw new EmptyInputException(POST_POST_EMPTY_CONTENTS);
            if(requestDto.getIsNotice())
                throw new BusinessLogicException(BUSINESS_LOGIC_ERROR);

            return sendResponseHttpByJson(SUCCESS, "스터디 홍보 글 쓰기 성공", postService.savePost(userStudyIdx, requestDto));
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 스터디 공지 글쓰기
    @PostMapping("/notice/{userStudyIdx}")
    public ResponseEntity<ResponseApiMessage> saveNotice(@PathVariable Long userStudyIdx, @RequestBody PostSaveRequestDto requestDto) {
        try {
            if(requestDto.getPostTitle().isEmpty())
                throw new EmptyInputException(POST_POST_EMPTY_TITLE);
            if(requestDto.getPostContents().isEmpty())
                throw new EmptyInputException(POST_POST_EMPTY_CONTENTS);
            if(!requestDto.getIsNotice())
                throw new BusinessLogicException(BUSINESS_LOGIC_ERROR);

            return sendResponseHttpByJson(SUCCESS, "스터디 공지 글 쓰기 성공", postService.savePost(userStudyIdx, requestDto));
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 스터디 홍보 게시판 검색
    @GetMapping("/promotion/search/{searchStr}")
    public ResponseEntity<ResponseApiMessage> searchPromotion(@PathVariable String searchStr, @PageableDefault(size = 5, sort = "postIndex", direction = Sort.Direction.ASC) Pageable pageable) {
        try {
            return sendResponseHttpByJson(SUCCESS, "스터디 아이디로 검색 성공", postService.searchPromotionByTitleAndContents(searchStr, pageable));
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 스터디 아이디로 검색 & 초대 - 스터디
    @PostMapping("/members/invite/{userStudyIdx}/{id}")
    public ResponseEntity<ResponseApiMessage> inviteStudy(@PathVariable Long userStudyIdx, @PathVariable String id) {
        try {
            return sendResponseHttpByJson(SUCCESS, "스터디에 유저 초대 성공.", userStudyService.inviteStudy(userStudyIdx, id));
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 스터디 초대 취소 - 스터디
    @DeleteMapping("/members/invite/admin/{adminUserStudyIdx}/{targetUserStudyIdx}")
        public ResponseEntity<ResponseApiMessage> cancelInvitedStudy(@PathVariable Long adminUserStudyIdx, @PathVariable Long targetUserStudyIdx) {
        try {
            userStudyService.deleteUserStudy(adminUserStudyIdx, targetUserStudyIdx, true);
            return sendResponseHttpByJson(SUCCESS, "스터디 초대 취소 성공.", null);
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 초대 수락 - 유저
    @PatchMapping("/members/invite/{myUserStudyIdx}")
    public ResponseEntity<ResponseApiMessage> acceptRequest(@PathVariable Long myUserStudyIdx) {
        try {
            return sendResponseHttpByJson(SUCCESS, "스터디 초대 수락 성공.",
                    userStudyService.updateUserStudy(null, myUserStudyIdx, false, 2));
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 초대 거절 - 유저
    @DeleteMapping("/members/invite/{myUserStudyIdx}")
    public ResponseEntity<ResponseApiMessage> rejectRequest(@PathVariable Long myUserStudyIdx) {
        try {
            userStudyService.deleteUserStudy(null, myUserStudyIdx, false);
            return sendResponseHttpByJson(SUCCESS, "스터디 초대 거절 성공.", null);
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 스터디 초대 목록 조회
    @GetMapping("/members/invite/{studyIdx}")
    public ResponseEntity<ResponseApiMessage> getInviteMembers(@PathVariable Long studyIdx) {
        try {
            return sendResponseHttpByJson(SUCCESS, "초대한 유저 목록 조회 성공.",
                    studyService.getStudyAppliersOrInvitee(studyIdx, 3));
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 스터디 가입 요청 목록 조회
    @GetMapping("/members/enroll/{studyIdx}")
    public ResponseEntity<ResponseApiMessage> getJoinMembers(@PathVariable Long studyIdx) {
        try {
            return sendResponseHttpByJson(SUCCESS, "참가 요청 받은 유저 목록 조회 성공.",
                    studyService.getStudyAppliersOrInvitee(studyIdx, 4));
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }


    // 참가 요청 보내기 - 유저
    @PostMapping("/members/enroll/{studyIdx}")
    public ResponseEntity<ResponseApiMessage> joinRequestStudy(@AuthenticationPrincipal User tokenUser, @PathVariable Long studyIdx) {
        try {
            return sendResponseHttpByJson(SUCCESS, "스터디 참가 요청 성공.",
                    userStudyService.saveUserStudy(4, tokenUser.getUserIndex(), studyIdx));
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 참가 요청 취소 - 유저
    @DeleteMapping("/members/enroll/{myUserStudyIdx}")
    public ResponseEntity<ResponseApiMessage> cancelJoinRequestStudy(@PathVariable Long myUserStudyIdx) {
        try {
            userStudyService.deleteUserStudy(null, myUserStudyIdx, false);
            return sendResponseHttpByJson(SUCCESS, "스터디 참가 요청 취소 성공.", null);
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 참가 요청 받은 스터디 가입 수락 - 스터디
    @PatchMapping("/members/enroll/{adminUserStudyIdx}/{targetUserStudyIdx}")
    public ResponseEntity<ResponseApiMessage> acceptInvitedStudy(@PathVariable Long adminUserStudyIdx, @PathVariable Long targetUserStudyIdx) {
        try {
            return sendResponseHttpByJson(SUCCESS, "스터디 가입 요청 수락 성공.",
                    userStudyService.updateUserStudy(adminUserStudyIdx, targetUserStudyIdx, true, 2));
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 참가 요청 받은 스터디 가입 거절 - 스터디
    @DeleteMapping("/members/enroll/admin/{adminUserStudyIdx}/{targetUserStudyIdx}")
    public ResponseEntity<ResponseApiMessage> rejectInvitedStudy(@PathVariable Long adminUserStudyIdx, @PathVariable Long targetUserStudyIdx) {
        try {
            userStudyService.deleteUserStudy(adminUserStudyIdx, targetUserStudyIdx, true);
            return sendResponseHttpByJson(SUCCESS, "스터디 가입 요청 거절 성공.", null);
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 스터디 멤버 추방
    @DeleteMapping("/members/admin/{adminUserStudyIdx}/{targetUserStudyIdx}")
    public ResponseEntity<ResponseApiMessage> kickMember(@PathVariable Long adminUserStudyIdx, @PathVariable Long targetUserStudyIdx) {
        try {
            userStudyService.deleteUserStudy(adminUserStudyIdx, targetUserStudyIdx, true);
            UserStudy userStudy = userStudyService.getUserStudy(adminUserStudyIdx);
            userStudyService.updateStudyNum(userStudy, false);
            return sendResponseHttpByJson(SUCCESS, "스터디 추방 성공.", null);
        } catch (BaseException e) {
            e.printStackTrace();
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 스터디 나가기
    @DeleteMapping(value = {"/members/{myUserStudyIdx}/{newLeaderUserStudyIdx}"
                            , "/members/{myUserStudyIdx}"})
    public ResponseEntity<ResponseApiMessage> leaveStudy(@PathVariable Long myUserStudyIdx, @PathVariable(required = false) Long newLeaderUserStudyIdx) {
        try {
            UserStudy userStudy = userStudyService.getUserStudy(myUserStudyIdx);
            // 멤버 인원 수 줄이기
            userStudyService.updateStudyNum(userStudy, false);

            // 새 리더 위임
            if(userStudy.getUserstudyMemberStatus() == 1) {
                UserStudy newLeader = userStudyService.getUserStudy(newLeaderUserStudyIdx);
                userStudyService.updateStudyMemberStatus(newLeader, 1);
            }

            // 삭제
            userStudyService.deleteUserStudy(null, myUserStudyIdx, false);
            return sendResponseHttpByJson(SUCCESS, "스터디 초대 나가기 성공.", null);
        } catch (BaseException e) {
            e.printStackTrace();
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 스터디 리더 위임
    @PatchMapping("/member/{myUserStudyIdx}/{newLeaderUserStudyIdx}")
    public  ResponseEntity<ResponseApiMessage> updateNewLeader(@PathVariable Long myUserStudyIdx, @PathVariable Long newLeaderUserStudyIdx) {
        try {
            UserStudy myUserStudy = userStudyService.getUserStudy(myUserStudyIdx);
            if(myUserStudy.getUserstudyMemberStatus() != 1)
                throw new BusinessLogicException(BUSINESS_LOGIC_ERROR);

            UserStudy newLeaderUserStudy = userStudyService.getUserStudy(newLeaderUserStudyIdx);
            userStudyService.updateStudyMemberStatus(myUserStudy, 2);
            userStudyService.updateStudyMemberStatus(newLeaderUserStudy, 1);

            return sendResponseHttpByJson(SUCCESS, "스터디 리더 위임 성공.", null);
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 스터디 삭제
    @DeleteMapping("/{myUserStudyIdx}")
    public ResponseEntity<ResponseApiMessage> deleteStudy(@PathVariable Long myUserStudyIdx) {
        try {
            UserStudy userStudy = userStudyService.getUserStudy(myUserStudyIdx);

            if(!userStudy.getUserstudyMemberStatus().equals(1))
                throw new NotAuthorizedAccessException(NOT_AUTHORIZED);

            studyService.deleteStudy(userStudy.getStudy());
            return sendResponseHttpByJson(SUCCESS, "스터디 삭제 성공.", null);
        } catch (BaseException e) {
            e.printStackTrace();
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 스터디 일정 참/불참
    @PatchMapping("/projects/attend/{projectIdx}/{memberIdx}")
        public ResponseEntity<ResponseApiMessage> updateIsAttendProject(@PathVariable Long projectIdx, @PathVariable Long memberIdx) {
        try {
            Project project = projectService.getProject(projectIdx);
            Study study = project.getStudy();

            Project member = null;
            for (Project studyProject : study.getStudyProjects()) {
                if(studyProject.getProjectNumber().equals(project.getProjectNumber())
                        && studyProject.getUser().getUserIndex().equals(memberIdx)) {
                    projectService.updateIsAttend(studyProject.getProjectIndex());
                    member = studyProject;
                }
            }

            return sendResponseHttpByJson(SUCCESS, "스터디 참여 여부 변경 성공.", member);
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 스터디 플젝 만들기
    @PostMapping("/projects/{userStudyIdx}")
    public ResponseEntity<ResponseApiMessage> saveStudyProject(@PathVariable Long userStudyIdx, @RequestBody ProjectSaveRequestDto requestDto) {
        try {
            // input
            if(requestDto.getName() == null)
                throw new EmptyInputException(PROJECT_EMPTY_NAME);
            if(requestDto.getIsStudy() == null)
                throw new EmptyInputException(PROJECT_EMPTY_STATUS);
            if(requestDto.getStartTime() == null)
                throw new EmptyInputException(PROJECT_EMPTY_STARTTIME);
            if(requestDto.getEndTime() == null)
                throw new EmptyInputException(PROJECT_EMPTY_ENDTIME);
            if(!requestDto.getIsStudy() ||
                    requestDto.getStudyIdx() == null)
                throw new BusinessLogicException(BUSINESS_LOGIC_ERROR);

            UserStudy userStudy = userStudyService.getUserStudy(userStudyIdx);
            if(!userStudy.getUserstudyMemberStatus().equals(1))
                throw new NotAuthorizedAccessException(NOT_AUTHORIZED);

            // 팀원들 모두 가져와서 생성
            Long biggestNum = 0L;
            for (Project studyProject : userStudy.getStudy().getStudyProjects()) {
                if(studyProject.getProjectNumber() > biggestNum) {
                    biggestNum = studyProject.getProjectNumber();
                }
            } biggestNum++;

            Project project = null;
            for (UserStudy studyUserstudy : userStudy.getStudy().getStudyUserstudies()) {
                if(studyUserstudy.getUserstudyMemberStatus() >= 3)
                    continue;

                User user = studyUserstudy.getUser();

                project = projectService.saveProject(requestDto, user, true, biggestNum);
                user.addProject(project);
                userStudy.getStudy().addProject(project);
            }

            return sendResponseHttpByJson(SUCCESS, "스터디 일정 생성 성공", project);
        } catch (BaseException e) {
            e.printStackTrace();
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 스터디 일정 수정: 스터디 탭에서 스터디 장만 수정 가능, 개인 플래너에서는 수정 불가능
    @PatchMapping("/projects/{userStudyIdx}/{projectIdx}")
    public ResponseEntity<ResponseApiMessage> updateStudyProject(@PathVariable Long userStudyIdx, @PathVariable Long projectIdx, @RequestBody ProjectUpdateRequestDto requestDto) {
        try {
            if(requestDto.getNewName() == null)
                throw new EmptyInputException(PROJECT_EMPTY_NAME);
            if(requestDto.getNewStartTime() == null)
                throw new EmptyInputException(PROJECT_EMPTY_STARTTIME);
            if(requestDto.getNewEndTime() == null)
                throw new EmptyInputException(PROJECT_EMPTY_ENDTIME);
            UserStudy userStudy = userStudyService.getUserStudy(userStudyIdx);
            if(!userStudy.getUserstudyMemberStatus().equals(1))
                throw new NotAuthorizedAccessException(NOT_AUTHORIZED);

            Study study = userStudy.getStudy();
            Project project = projectService.getProject(projectIdx);
            for (Project studyProject : study.getStudyProjects()) {
                if(project.getProjectNumber().equals(studyProject.getProjectNumber()))
                    projectService.updateProject(studyProject.getProjectIndex(), requestDto, true, userStudyIdx);
            }

            return sendResponseHttpByJson(SUCCESS, "스터디 일정 수정 성공", project);
        } catch (BaseException e) {
            e.printStackTrace();
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 일정 삭제
    @DeleteMapping("/projects/{userStudyIdx}/{projectIdx}")
    public ResponseEntity<ResponseApiMessage> deleteStudyProject(@PathVariable Long userStudyIdx, @PathVariable Long projectIdx) {
        try {
            if(!userStudyService.getUserStudy(userStudyIdx).getUserstudyMemberStatus().equals(1))
                throw new NotAuthorizedAccessException(NOT_AUTHORIZED);
            Project project = projectService.getProject(projectIdx);
            Study study = project.getStudy();

            for (Project studyProject : study.getStudyProjects()) {
                if(project.getProjectNumber().equals(studyProject.getProjectNumber()))
                    projectService.deleteProject(studyProject.getProjectIndex());
            }

            return sendResponseHttpByJson(SUCCESS, "스터디 일정 삭제 성공", null);
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }
}
