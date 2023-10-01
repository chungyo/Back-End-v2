package com.mmos.mmos.src.service;

import com.mmos.mmos.config.HttpResponseStatus;
import com.mmos.mmos.src.domain.dto.calendar.CalendarGetRequestDto;
import com.mmos.mmos.src.domain.dto.calendar.CalendarResponseDto;
import com.mmos.mmos.src.domain.entity.Calendar;
import com.mmos.mmos.src.domain.entity.Plan;
import com.mmos.mmos.src.domain.entity.Project;
import com.mmos.mmos.src.domain.entity.User;
import com.mmos.mmos.src.repository.CalendarRepository;
import com.mmos.mmos.src.repository.PlanRepository;
import com.mmos.mmos.src.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.mmos.mmos.config.HttpResponseStatus.POST_CALENDAR_INVALID_REQUEST;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final CalendarRepository calendarRepository;
    private final UserRepository userRepository;
    private final PlanRepository planRepository;

    public User findUserByIdx(Long userIdx) {
        return userRepository.findById(userIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다. USER_INDEX=" + userIdx));
    }

    public Calendar findCalendarByMonthAndYear(Long userIdx, Integer calendarYear, Integer calendarMonth) {
        return calendarRepository.findCalendarByUser_UserIndexAndCalendarYearAndCalendarMonth(userIdx,calendarYear,calendarMonth)
                .orElse( null);
    }

    public List<Plan> findPlansIsVisible(Calendar calendar){
        return planRepository.findPlansByPlanIsVisibleIsTrueAndPlanner_Calendar(calendar)
                .orElse(null);
    }

    // 캘린더 생성
    @Transactional
    public CalendarResponseDto saveCalendar(Integer year, Integer month, Long userIdx) {
        User user = findUserByIdx(userIdx);

        // 막 회원 가입을 한 유저가 아니면서 같은 달의 캘린더가 이미 존재할 때 생성 막기
        if (!user.getUserCalendars().isEmpty() && findCalendarByMonthAndYear(userIdx,year,month) != null)
            return new CalendarResponseDto(POST_CALENDAR_INVALID_REQUEST);

        System.out.println("year = " + year + "month = " + month + "will be added");

        Calendar calendar = new Calendar(year, month, user);
        // User, Calendar 양방향 매핑
        user.addCalendars(calendar);

        System.out.println("calendar = " + calendar);
        System.out.println("user = " + user);

        calendarRepository.save(calendar);

        return new CalendarResponseDto(calendar, HttpResponseStatus.SUCCESS, null, null);
    }

    @Transactional
    public CalendarResponseDto getCalendar(Long userIdx, CalendarGetRequestDto calendarGetRequestDto){
        // user, calendar, List<Project> 객체 가져오기
        User user = findUserByIdx(userIdx);
        Calendar calendar = findCalendarByMonthAndYear(userIdx,calendarGetRequestDto.getYear(), calendarGetRequestDto.getMonth());
        List<Project> userProjectList = user.getUserProjects();

        // 해당 달을 포함하는 Project 찾기
        List<Project> calendarProjectList = new ArrayList<>();
        for (Project project : userProjectList){
            if(project.getProjectStartTime().getMonthValue()<= calendarGetRequestDto.getMonth() && project.getProjectEndTime().getYear()>=calendarGetRequestDto.getYear())
                calendarProjectList.add(project);
        }

        // 해당 달에서 planIsVisible == true인 plan들을 리스트로 가져오기
        List<Plan> planList = findPlansIsVisible(calendar);

         return new CalendarResponseDto(calendar, HttpResponseStatus.SUCCESS, calendarProjectList, planList);
    }
}
