package com.mmos.mmos.src.service;

import com.mmos.mmos.src.domain.dto.calendar.CalendarGetProjectRequestDto;
import com.mmos.mmos.src.domain.dto.project.ProjectResponseDto;
import com.mmos.mmos.src.domain.dto.project.ProjectSaveRequestDto;
import com.mmos.mmos.src.domain.entity.Calendar;
import com.mmos.mmos.src.domain.entity.Project;
import com.mmos.mmos.src.domain.entity.User;
import com.mmos.mmos.src.repository.CalendarRepository;
import com.mmos.mmos.src.repository.ProjectRepository;
import com.mmos.mmos.src.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final CalendarRepository calendarRepository;
    private final CalendarService calendarService;

    public User findUser(Long userIdx){
        return userRepository.findById(userIdx)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 유저입니다."));
    }

    public Calendar findCalendar(Long userIdx,int year, int month){
        return calendarRepository.findCalendarByUser_UserIndexAndCalendarYearAndCalendarMonth(userIdx,year,month)
                .orElse(null);
    }

    public ProjectResponseDto saveProject(Long userIdx, ProjectSaveRequestDto projectSaveRequestDto){
        // user 찾기

        User user = findUser(userIdx);
        // 프로젝트 시작시각부터 종료시각까지 필요한 달 체크. 없으면 생성.
        LocalDate startTime = projectSaveRequestDto.getStartTime();
        LocalDate endTime = projectSaveRequestDto.getEndTime();
        for (LocalDate time = startTime; (time.getMonthValue() <= endTime.getMonthValue()) || (time.getYear() < endTime.getYear()); time = time.plusMonths(1)){
            Calendar calendar = findCalendar(userIdx, time.getYear(),time.getMonthValue());
            System.out.println("calendar = " + calendar);
            if(calendar == null){
                calendarService.saveCalendar(time.getYear(),time.getMonthValue(),userIdx);
            }
        }

        // 프로젝트 생성
        Project project = new Project(startTime,endTime,projectSaveRequestDto.getName(),user);

        // User에 프로젝트 저장
        user.getUserProjects().add(project);

        return new ProjectResponseDto(projectRepository.save(project));
    }


    // return 값 보류 (미완성)
    @Transactional
    public List<ProjectResponseDto> getProjects(Long userIdx, CalendarGetProjectRequestDto calendarGetProjectRequestDto){


        User user = findUser(userIdx);


        List<Project> projects = projectRepository.findAllByUser(user);

        // for로 날짜 비교

        List<ProjectResponseDto> projectResponseDtoList = new ArrayList<>();


        return projectResponseDtoList;
    }
}
