package com.mmos.mmos.src.service;

import com.mmos.mmos.config.HttpResponseStatus;
import com.mmos.mmos.src.domain.dto.project.ProjectNameUpdateDto;
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

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final CalendarRepository calendarRepository;
    private final CalendarService calendarService;

    public User findUser(Long userIdx) {
        return userRepository.findById(userIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
    }

    public Calendar findCalendar(Long userIdx, int year, int month) {
        return calendarRepository.findCalendarByUser_UserIndexAndCalendarYearAndCalendarMonth(userIdx, year, month)
                .orElse(null);
    }

    public Project findProject(Long projectIdx){
        return projectRepository.findById(projectIdx)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 프로젝트입니다."));
    }

    @Transactional
    public ProjectResponseDto saveProject(Long userIdx, ProjectSaveRequestDto projectSaveRequestDto) {
        // user 찾기

        User user = findUser(userIdx);
        // 프로젝트 시작시각부터 종료시각까지 필요한 달 체크. 없으면 생성.
        LocalDate startTime = projectSaveRequestDto.getStartTime();
        LocalDate endTime = projectSaveRequestDto.getEndTime();
        for (LocalDate time = startTime; (time.getMonthValue() <= endTime.getMonthValue()) || (time.getYear() < endTime.getYear()); time = time.plusMonths(1)) {
            Calendar calendar = findCalendar(userIdx, time.getYear(), time.getMonthValue());
            System.out.println("calendar = " + calendar);
            if (calendar == null) {
                calendarService.saveCalendar(time.getYear(), time.getMonthValue(), userIdx);
            }
        }

        // 프로젝트 생성
        Project project = new Project(startTime, endTime, projectSaveRequestDto.getName(), user);

        // User에 프로젝트 저장
        user.getUserProjects().add(project);

        return new ProjectResponseDto(projectRepository.save(project), HttpResponseStatus.SUCCESS);
    }

    @Transactional
    public ProjectResponseDto updateProjectName(Long userIdx, Long projectIdx, ProjectNameUpdateDto projectNameUpdateDto) {
        // 프로젝트를 소유한 유저인지 확인
        Project project = findProject(projectIdx);
        User user = findUser(userIdx);
        if(!user.getUserProjects().contains(project)){
            return new ProjectResponseDto(HttpResponseStatus.UPDATE_PROJECT_NOT_OWNER);
        }

        project.updateProjectName(projectNameUpdateDto.getNewName());

        return new ProjectResponseDto(project,HttpResponseStatus.SUCCESS);
    }
}
