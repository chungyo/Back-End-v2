package com.mmos.mmos.src.service;

import com.mmos.mmos.src.domain.dto.planner.PlannerResponseDto;
import com.mmos.mmos.src.domain.entity.Calendar;
import com.mmos.mmos.src.domain.entity.Planner;
import com.mmos.mmos.src.domain.entity.Project;
import com.mmos.mmos.src.domain.entity.User;
import com.mmos.mmos.src.repository.CalendarRepository;
import com.mmos.mmos.src.repository.PlannerRepository;
import com.mmos.mmos.src.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.mmos.mmos.config.HttpResponseStatus.*;

@Service
@RequiredArgsConstructor
public class PlannerService {

    private final PlannerRepository plannerRepository;
    private final CalendarRepository calendarRepository;
    private final UserRepository userRepository;

    public Calendar findCalendarByIdx(Long calendarIdx) {
        return calendarRepository.findById(calendarIdx)
                .orElse(null);
    }

    public Planner findPlannerByIdx(Long plannerIdx){
        return plannerRepository.findById(plannerIdx)
                .orElse(null);
    }

    public User findUserByIdx(Long userIdx) {
        return userRepository.findById(userIdx)
                .orElse(null);
    }

    // 플래너 생성
    @Transactional
    public PlannerResponseDto savePlanner(LocalDate today, Long calendarIdx) {
        Calendar calendar = findCalendarByIdx(calendarIdx);
        if(calendar == null)
            return new PlannerResponseDto(EMPTY_CALENDAR);

        // 막 회원 가입을 한 유저가 아니면서 같은 날의 플래너가 이미 존재할 때 생성 막기
        if(plannerRepository.findPlannerByCalendar_CalendarIndexAndPlannerDate(calendarIdx, today).isPresent())
            return null;

        Planner planner = new Planner(today, calendar);
        // Calendar, Planner 양방향 매핑
        calendar.addPlanner(planner);

        plannerRepository.save(planner);

        return new PlannerResponseDto(planner,null, SUCCESS);
    }


    // 플래너 메모
    @Transactional
    public PlannerResponseDto setMemo(Long plannerIdx, String plannerMemo) {
        Planner planner = findPlannerByIdx(plannerIdx);  // 플래너 찾기
        if(planner == null)
            return new PlannerResponseDto(EMPTY_PLANNER);
        planner.setMemo(plannerMemo);  // 메모 설정
        plannerRepository.save(planner);  // 변경 사항 저장
        return new PlannerResponseDto(planner,null, SUCCESS);  // 변경된 플래너 반환
    }

    @Transactional
    public PlannerResponseDto getPlanner(Long plannerIdx, Long userIdx) {
        Planner planner = findPlannerByIdx(plannerIdx);
        if(planner == null)
            return new PlannerResponseDto(EMPTY_PLANNER);

        User user = findUserByIdx(userIdx);
        if(user == null)
            return new PlannerResponseDto(EMPTY_USER);

        List<Project> userProjectList = user.getUserProjects();

        // 해당 날짜를 포함하는 Project 찾기
        List<Project> plannerProjectList = new ArrayList<>();
        LocalDate targetDate = planner.getPlannerDate();  // 특정 날짜 가져오기

        for (Project project : userProjectList){
            LocalDate projectStart = project.getProjectStartTime();
            LocalDate projectEnd = project.getProjectEndTime();

            // 특정 날짜가 프로젝트의 시작과 종료 사이에 있는지 확인
            if ((projectStart.isBefore(targetDate) || projectStart.isEqual(targetDate)) &&
                    (projectEnd.isAfter(targetDate) || projectEnd.isEqual(targetDate))) {
                plannerProjectList.add(project);
            }
        }

        PlannerResponseDto responseDto = new PlannerResponseDto(planner, plannerProjectList, SUCCESS);
        return responseDto;
    }
}
