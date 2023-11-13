package com.mmos.mmos.src.controller;

import com.mmos.mmos.config.ResponseApiMessage;
import com.mmos.mmos.config.exception.BaseException;
import com.mmos.mmos.config.exception.BusinessLogicException;
import com.mmos.mmos.config.exception.EmptyInputException;
import com.mmos.mmos.src.domain.dto.request.*;
import com.mmos.mmos.src.domain.dto.response.home.CalendarSectionDto;
import com.mmos.mmos.src.domain.dto.response.planner.PlannerPageResponseDto;
import com.mmos.mmos.src.domain.dto.response.planner.PlannerSectionDto;
import com.mmos.mmos.src.domain.entity.Plan;
import com.mmos.mmos.src.domain.entity.Planner;
import com.mmos.mmos.src.domain.entity.Project;
import com.mmos.mmos.src.domain.entity.User;
import com.mmos.mmos.src.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static com.mmos.mmos.config.HttpResponseStatus.*;

@RestController
@RequestMapping("/planner")
@RequiredArgsConstructor
public class PlannerPageController extends BaseController {

    private final UserService userService;
    private final CalendarService calendarService;
    private final PlannerService plannerService;
    private final ProjectService projectService;
    private final PlanService planService;
    private final StudyService studyService;

    /**
     * 플래너 페이지 전체를 구성하는 모든 컬럼들 조회 API
     *      + 캘린더 안에 날짜를 클릭했을 때 보내지는 쿼리
     * @param userIdx: 로그인한 사용자 인덱스
     * @param day: 조회하고 싶은 날짜
     */
    @GetMapping("")
    public ResponseEntity<ResponseApiMessage> getPage(@RequestParam Long userIdx, @RequestParam LocalDate day) {
        try {
            // 객체 가져오기
            CalendarSectionDto calendar =
                    calendarService.getCalendar(userIdx, new CalendarGetRequestDto(day.getMonthValue(), day.getYear()));
            List<Project> projects = projectService.getProjects(calendar.getIdx(), day.getDayOfMonth());
            try {
                Planner planner = plannerService.getPlannerByCalendarAndDate(calendar.getIdx(), day);
                return sendResponseHttpByJson(SUCCESS, "플래너 페이지 로드 성공", new PlannerPageResponseDto(calendar, planner, projects));
            } catch (BaseException e) {
                return sendResponseHttpByJson(SUCCESS, "플래너 페이지 로드 성공", new PlannerPageResponseDto(calendar, projects));
            }
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    /**
     * 할 일 생성 버튼 클릭했을 때 보내지는 API
     * @param userIdx: 할 일 생성하려는 사용자 인덱스
     * @param requestDto
     *          - String planName: 계획명
     *          - Boolean isStudy: 스터디 계획 여부
     *          - Long userStudyIdx: 내 스터디 인덱스
     *          - LocalDate date: 계획을 생성하려는 날짜
     */
    @PostMapping("plans")
    public ResponseEntity<ResponseApiMessage> savePlan(@RequestParam Long userIdx, @RequestBody PlanSaveRequestDto requestDto) {
        try {
            // Validation: input
            if(requestDto.getPlanName() == null)
                throw new EmptyInputException(PLAN_EMPTY_NEWNAME);
            if(requestDto.getIsStudy() == null)
                throw new EmptyInputException(PLAN_EMPTY_STATUS);
            if(requestDto.getDate() == null)
                throw new EmptyInputException(PLAN_EMPTY_DATE);
            if((requestDto.getIsStudy() && requestDto.getUserStudyIdx() == null) ||
                    (!requestDto.getIsStudy() && requestDto.getUserStudyIdx() != null))
                throw new BusinessLogicException(BUSINESS_LOGIC_ERROR);

            // Business Logic
            CalendarSectionDto calendar =
                    calendarService.getCalendar(userIdx, new CalendarGetRequestDto(requestDto.getDate().getMonthValue(),
                                                                                    requestDto.getDate().getYear()));
            Planner planner;
            try{
                planner = plannerService.getPlannerByCalendarAndDate(calendar.getIdx(), requestDto.getDate());
            } catch (BaseException e) {
                planner = plannerService.savePlanner(calendar.getIdx(), requestDto.getDate());
            }

            planService.savePlan(planner.getPlannerIndex(), requestDto);

            return sendResponseHttpByJson(SUCCESS, "계획 생성 성공", new PlannerSectionDto(planner));
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    } /* public ResponseEntity<ResponseApiMessage> savePlan */

    /**
     * 계획 수정하는 API
     * @param planIdx: 수정하려는 계획 인덱스
     * @param requestDto
     *          - String newName: 변경하려는 이름
     */
    @PatchMapping("/plans")
    public ResponseEntity<ResponseApiMessage> updatePlan(@RequestParam Long planIdx, @RequestBody PlanUpdateRequestDto requestDto) {
       try {
           if(requestDto.getNewName() == null)
               throw new EmptyInputException(PLAN_EMPTY_NEWNAME);

            Plan plan = planService.updatePlan(planIdx, requestDto);
            return sendResponseHttpByJson(SUCCESS, "계획 수정 성공", new PlannerSectionDto(plan.getPlanner()));
       } catch (BaseException e) {
           return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
       }
    }

    /**
     * 계획 완수 여부 설정하는 API
     * @param planIdx: 설정하려는 계획 인덱스
     */
    @PatchMapping("/plans/complete")
    public ResponseEntity<ResponseApiMessage> checkPlanIsComplete(@RequestParam Long planIdx) {
        try {
            Plan plan = planService.updatePlanIsComplete(planIdx);
            return sendResponseHttpByJson(SUCCESS, "계획 완수 여부 수정 성공", new PlannerSectionDto(plan.getPlanner()));
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    /**
     * 계획 삭제하는 API
     * @param planIdx: 삭제하려는 계획 인덱스
     */
    // 계획 삭제
    @DeleteMapping("/plans")
    public ResponseEntity<ResponseApiMessage> deletePlan(@RequestParam Long planIdx) {
        try {
            Plan plan = planService.deletePlan(planIdx);
            return sendResponseHttpByJson(SUCCESS, "계획 삭제 성공", new PlannerSectionDto(plan.getPlanner()));
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    /**
     * 프로젝트 생성하는 API
     * @param userIdx: 사용자 인덱스
     * @param requestDto
     *          - LocalDate startTime: 프로젝트 시작 시간
     *          - LocalDate endTime: 프로젝트 마감 시간
     *          - String name: 프로젝트 이름
     *          - Boolean isStudy: 스터디 프로젝트 여부
     *          - Long studyIdx: 스터디 인덱스
     */
    // 프로젝트 생성
    @PostMapping("/projects")
    public ResponseEntity<ResponseApiMessage> saveProject(@RequestParam Long userIdx, @RequestBody ProjectSaveRequestDto requestDto) {
        try {
            // Validation: input
            if(requestDto.getName() == null)
                throw new EmptyInputException(PROJECT_EMPTY_NAME);
            if(requestDto.getIsStudy() == null)
                throw new EmptyInputException(PROJECT_EMPTY_STATUS);
            if(requestDto.getStartTime() == null)
                throw new EmptyInputException(PROJECT_EMPTY_STARTTIME);
            if(requestDto.getEndTime() == null)
                throw new EmptyInputException(PROJECT_EMPTY_ENDTIME);
            if(requestDto.getIsStudy() ||
                    requestDto.getStudyIdx() != null)
                throw new BusinessLogicException(BUSINESS_LOGIC_ERROR);

            // Business Logic
            User user = userService.getUser(userIdx);

            return sendResponseHttpByJson(SUCCESS, "프로젝트 생성 성공", projectService.saveProject(requestDto, user, false, null));
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    } /* public ResponseEntity<ResponseApiMessage> saveProject */

    /**
     * 프로젝트 이름 수정하는 API
     * @param projectIdx: 수정하려는 프로젝트 인덱스
     * @param requestDto
     *          - String newName: 변경하려는 이름
     */
    // 프로젝트 수정
    @PatchMapping("/projects")
    public ResponseEntity<ResponseApiMessage> updateProject(@RequestParam Long projectIdx, @RequestBody ProjectUpdateRequestDto requestDto) {
        try {
            return sendResponseHttpByJson(SUCCESS, "계획 수정 성공", projectService.updateProject(projectIdx, requestDto, false, null));
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    /**
     * 프로젝트 완수 여부 설정 API
     * @param projectIdx: 설정하려는 프로젝트 인덱스
     */
    // 프로젝트 완수 버튼
    @PatchMapping("/projects/complete")
    public ResponseEntity<ResponseApiMessage> checkProjectIsComplete(@RequestParam Long projectIdx) {
        try {
            return sendResponseHttpByJson(SUCCESS, "프로젝트 완수 여부 수정 성공", projectService.updateProjectIsComplete(projectIdx));
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    /**
     * 프로젝트 달력에 보이기 설정 API
     * @param projectIdx: 설정하려는 프로젝트 인덱스
     */
    // 프로젝트 달력에 보이게 설정
    @PatchMapping("/projects/display")
    public ResponseEntity<ResponseApiMessage> checkProjectIsVisible(@RequestParam Long projectIdx) {
        try {
            return sendResponseHttpByJson(SUCCESS, "프로젝트 표시 여부 수정 성공", projectService.updateProjectIsVisible(projectIdx));
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    /**
     * 프로젝트 삭제하는 API
     * @param projectIdx: 삭제하려는 프로젝트 인덱스
     */
    // 프로젝트 삭제
    @DeleteMapping("/projects")
    public ResponseEntity<ResponseApiMessage> deleteProject(@RequestParam Long projectIdx) {
        try {
            projectService.deleteProject(projectIdx);
            return sendResponseHttpByJson(SUCCESS, "프로젝트 삭제 성공", null);
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    /**
     * 공부시간 시작 & 마감 설정하는 API
     * @param planIdx: 공부시간 계획 인덱스
     */
    // 공부 시간 설정
    @PatchMapping("/time")
    public ResponseEntity<ResponseApiMessage> setStudyTime(@RequestParam Long planIdx) {
        try {
            Plan plan = planService.setStudyTime(planIdx);
            return sendResponseHttpByJson(SUCCESS, "계획 공부시간 설정 성공", new PlannerSectionDto(plan.getPlanner()));
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }
}
