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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static com.mmos.mmos.config.HttpResponseStatus.*;

@RestController
@RequestMapping("/api/v1/planner")
@RequiredArgsConstructor
public class PlannerPageController extends BaseController {

    private final UserService userService;
    private final CalendarService calendarService;
    private final PlannerService plannerService;
    private final ProjectService projectService;
    private final PlanService planService;

    @GetMapping("/{date}")
    public ResponseEntity<ResponseApiMessage> getPage(@AuthenticationPrincipal User tokenUser, @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        try {
            // 객체 가져오기
            CalendarSectionDto calendar =
                    calendarService.getCalendar(tokenUser.getUserIndex(), new CalendarGetRequestDto(date.getMonthValue(), date.getYear()));
            List<Project> projects = projectService.getProjects(calendar.getIdx(), date.getDayOfMonth());
            try {
                Planner planner = plannerService.getPlannerByCalendarAndDate(calendar.getIdx(), date);
                return sendResponseHttpByJson(SUCCESS, "플래너 페이지 로드 성공", new PlannerPageResponseDto(calendar, planner, projects));
            } catch (BaseException e) {
                return sendResponseHttpByJson(SUCCESS, "플래너 페이지 로드 성공", new PlannerPageResponseDto(calendar, projects));
            }
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    @PostMapping("plans")
    public ResponseEntity<ResponseApiMessage> savePlan(@AuthenticationPrincipal User tokenUser, @RequestBody PlanSaveRequestDto requestDto) {
        try {
            // Validation: input
            if(requestDto.getPlanName() == null)
                throw new EmptyInputException(PLAN_EMPTY_NEWNAME);
            if(requestDto.getDate() == null)
                throw new EmptyInputException(PLAN_EMPTY_DATE);

            // Business Logic
            CalendarSectionDto calendar =
                    calendarService.getCalendar(tokenUser.getUserIndex(), new CalendarGetRequestDto(requestDto.getDate().getMonthValue(),
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

    @PatchMapping("/plans/{planIdx}")
    public ResponseEntity<ResponseApiMessage> updatePlan(@PathVariable Long planIdx, @RequestBody PlanUpdateRequestDto requestDto) {
       try {
           if(requestDto.getNewName() == null)
               throw new EmptyInputException(PLAN_EMPTY_NEWNAME);

            Plan plan = planService.updatePlan(planIdx, requestDto);
            return sendResponseHttpByJson(SUCCESS, "계획 수정 성공", new PlannerSectionDto(plan.getPlanner()));
       } catch (BaseException e) {
           return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
       }
    }

    @PatchMapping("/plans/complete/{planIdx}")
    public ResponseEntity<ResponseApiMessage> checkPlanIsComplete(@PathVariable Long planIdx) {
        try {
            Plan plan = planService.updatePlanIsComplete(planIdx);
            return sendResponseHttpByJson(SUCCESS, "계획 완수 여부 수정 성공", new PlannerSectionDto(plan.getPlanner()));
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 공부 시간 설정
    @PatchMapping("/plans/time/{planIdx}")
    public ResponseEntity<ResponseApiMessage> setStudyTime(@PathVariable Long planIdx) {
        try {
            Plan plan = planService.setStudyTime(planIdx);
            return sendResponseHttpByJson(SUCCESS, "계획 공부시간 설정 성공", new PlannerSectionDto(plan.getPlanner()));
        } catch (BaseException e) {
            e.printStackTrace();
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 계획 삭제
    @DeleteMapping("/plans/{planIdx}")
    public ResponseEntity<ResponseApiMessage> deletePlan(@PathVariable Long planIdx) {
        try {
            Plan plan = planService.deletePlan(planIdx);
            return sendResponseHttpByJson(SUCCESS, "계획 삭제 성공", new PlannerSectionDto(plan.getPlanner()));
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 프로젝트 생성
    @PostMapping("/projects")
    public ResponseEntity<ResponseApiMessage> saveProject(@AuthenticationPrincipal User tokenUser, @RequestBody ProjectSaveRequestDto requestDto) {
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
            User user = userService.getUser(tokenUser.getUserIndex());

            return sendResponseHttpByJson(SUCCESS, "프로젝트 생성 성공", projectService.saveProject(requestDto, user, false, null));
        } catch (BaseException e) {
            e.printStackTrace();
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    } /* public ResponseEntity<ResponseApiMessage> saveProject */

    // 프로젝트 수정
    @PatchMapping("/projects/{projectIdx}")
    public ResponseEntity<ResponseApiMessage> updateProject(@PathVariable Long projectIdx, @RequestBody ProjectUpdateRequestDto requestDto) {
        try {
            return sendResponseHttpByJson(SUCCESS, "계획 수정 성공", projectService.updateProject(projectIdx, requestDto, false, null));
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 프로젝트 완수 버튼
    @PatchMapping("/projects/complete/{projectIdx}")
    public ResponseEntity<ResponseApiMessage> checkProjectIsComplete(@PathVariable Long projectIdx) {
        try {
            return sendResponseHttpByJson(SUCCESS, "프로젝트 완수 여부 수정 성공", projectService.updateProjectIsComplete(projectIdx));
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 프로젝트 달력에 보이게 설정
    @PatchMapping("/projects/display/{projectIdx}")
    public ResponseEntity<ResponseApiMessage> checkProjectIsVisible(@PathVariable Long projectIdx) {
        try {
            return sendResponseHttpByJson(SUCCESS, "프로젝트 표시 여부 수정 성공", projectService.updateProjectIsVisible(projectIdx));
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }

    // 프로젝트 삭제
    @DeleteMapping("/projects/{projectIdx}")
    public ResponseEntity<ResponseApiMessage> deleteProject(@PathVariable Long projectIdx) {
        try {
            projectService.deleteProject(projectIdx);
            return sendResponseHttpByJson(SUCCESS, "프로젝트 삭제 성공", null);
        } catch (BaseException e) {
            return sendResponseHttpByJson(e.getStatus(), e.getStatus().getMessage(), null);
        }
    }


}
