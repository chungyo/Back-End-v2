package com.mmos.mmos.src.service;

import com.mmos.mmos.config.HttpResponseStatus;
import com.mmos.mmos.src.domain.dto.planner.PlannerResponseDto;
import com.mmos.mmos.src.domain.entity.Calendar;
import com.mmos.mmos.src.domain.entity.Planner;
import com.mmos.mmos.src.domain.entity.Project;
import com.mmos.mmos.src.domain.entity.User;
import com.mmos.mmos.src.repository.CalendarRepository;
import com.mmos.mmos.src.repository.PlannerRepository;
import com.mmos.mmos.src.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlannerService {

    private final PlannerRepository plannerRepository;
    private final CalendarRepository calendarRepository;
    private final UserRepository userRepository;

    public Calendar findCalendarByIdx(Long calendarIdx) {
        return calendarRepository.findById(calendarIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 캘린더입니다. CALENDAR_INDEX=" + calendarIdx));
    }

    public Planner findPlannerByIdx(Long plannerIdx){
        return plannerRepository.findById(plannerIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 플래너입니다. PLANNER_ID= " + plannerIdx));
    }

    public User findUserByIdx(Long userIdx) {
        return userRepository.findById(userIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다. USER_INDEX=" + userIdx));
    }



    // 플래너 생성
    @Transactional
    public PlannerResponseDto savePlanner(LocalDate today, Long calendarIdx) {
        Calendar calendar = findCalendarByIdx(calendarIdx);

        // 막 회원 가입을 한 유저가 아니면서 같은 날의 플래너가 이미 존재할 때 생성 막기
        if(plannerRepository.findPlannerByCalendar_CalendarIndexAndPlannerDate(calendarIdx, today).isPresent())
            return null;

        Planner planner = new Planner(today, calendar);
        // Calendar, Planner 양방향 매핑
        calendar.addPlanner(planner);

        plannerRepository.save(planner);

        return new PlannerResponseDto(planner,null, HttpResponseStatus.SUCCESS);
    }


    // 플래너 메모
    @Transactional
    public PlannerResponseDto setMemo(Long plannerIdx, String plannerMemo) {
        Planner planner = findPlannerByIdx(plannerIdx);  // 플래너 찾기
        planner.setMemo(plannerMemo);  // 메모 설정
        plannerRepository.save(planner);  // 변경 사항 저장
        return new PlannerResponseDto(planner,null, HttpResponseStatus.SUCCESS);  // 변경된 플래너 반환
    }

    @Transactional
    public PlannerResponseDto getPlanner(Long plannerIdx, Long userIdx) {
        Planner planner = findPlannerByIdx(plannerIdx);
        User user = findUserByIdx(userIdx);
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
        // null검사
        if (planner == null) {
            throw new EntityNotFoundException("플래너를 찾을 수 없습니다. PLANNER_INDEX= " + plannerIdx);
        }

        PlannerResponseDto responseDto = new PlannerResponseDto(planner, plannerProjectList, HttpResponseStatus.SUCCESS);  // Assuming PlannerResponseDto constructor is modified to accept plannerProjectList

        return responseDto;
    }
}
