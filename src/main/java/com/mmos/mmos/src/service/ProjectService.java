package com.mmos.mmos.src.service;

import com.mmos.mmos.config.exception.*;
import com.mmos.mmos.src.domain.dto.request.CalendarGetRequestDto;
import com.mmos.mmos.src.domain.dto.request.ProjectSaveRequestDto;
import com.mmos.mmos.src.domain.dto.request.ProjectUpdateRequestDto;
import com.mmos.mmos.src.domain.dto.response.home.CalendarSectionDto;
import com.mmos.mmos.src.domain.entity.*;
import com.mmos.mmos.src.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.mmos.mmos.config.HttpResponseStatus.*;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final CalendarRepository calendarRepository;
    private final CalendarService calendarService;
    private final StudyRepository studyRepository;
    private final UserRepository userRepository;
    private final UserStudyRepository userStudyRepository;

    public User findUserByIdx(Long userIdx) throws BaseException {
        return userRepository.findById(userIdx)
                .orElseThrow(() -> new EmptyEntityException(EMPTY_USER));
    }

    public Study findStudyByIdx(Long studyIdx) throws BaseException {
        return studyRepository.findById(studyIdx)
                .orElseThrow(() -> new EmptyEntityException(EMPTY_STUDY));
    }

    public Calendar findCalendarByIdx(Long calendarIdx) throws BaseException {
        return calendarRepository.findById(calendarIdx)
                .orElseThrow(() -> new EmptyEntityException(EMPTY_CALENDAR));
    }

    public Project findProjectByIdx(Long projectIdx) throws BaseException {
        return projectRepository.findById(projectIdx)
                .orElseThrow(() -> new EmptyEntityException(EMPTY_PROJECT));
    }

    public Calendar findCalendarByUserIdxAndDate(Long projectIdx, Integer year, Integer month) throws BaseException {
        return calendarRepository.findCalendarByUser_UserIndexAndCalendarYearAndCalendarMonth(projectIdx, year, month)
                .orElseThrow(() -> new EmptyEntityException(EMPTY_CALENDAR));
    }

    public UserStudy findUserstudyByIdx(Long userStudyIdx) throws BaseException {
        return userStudyRepository.findById(userStudyIdx)
                .orElseThrow(() -> new EmptyEntityException(EMPTY_USERSTUDY));
    }

    @Transactional
    public Project getProject(Long projectIdx) throws BaseException {
        try {
            return findProjectByIdx(projectIdx);
        } catch (EmptyEntityException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public List<Project> getProjects(Long calendarIdx, Integer day) throws BaseException {
        try {
            Calendar calendar = findCalendarByIdx(calendarIdx);
            User user = calendar.getUser();

            List<Project> projects = user.getUserProjects();

            // 해당 날짜를 포함하는 Project 찾기
            List<Project> plannerProjectList = new ArrayList<>();
            LocalDate targetDate = LocalDate.of(calendar.getCalendarYear(), calendar.getCalendarMonth(), day);  // 특정 날짜 가져오기

            for (Project project : projects) {
                LocalDate projectStart = project.getProjectStartTime();
                LocalDate projectEnd = project.getProjectEndTime();

                // 특정 날짜가 프로젝트의 시작과 종료 사이에 있는지 확인
                if ((projectStart.isBefore(targetDate) || projectStart.isEqual(targetDate)) &&
                        (projectEnd.isAfter(targetDate) || projectEnd.isEqual(targetDate))) {
                    plannerProjectList.add(project);
                }
            }

            return plannerProjectList;
        } catch (EmptyEntityException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public Project saveProject(ProjectSaveRequestDto requestDto, User user, boolean isStudy, Long studyNum) throws BaseException {
        try {
            LocalDate startTime = requestDto.getStartTime();
            LocalDate endTime = requestDto.getEndTime();

            if(requestDto.getStartTime().isAfter(requestDto.getEndTime()))
                throw new BusinessLogicException(BUSINESS_LOGIC_ERROR);

            for (LocalDate time = startTime;
                 (time.getMonthValue() <= endTime.getMonthValue()) && (time.getYear() <= endTime.getYear());
                 time = time.plusMonths(1)) {
                CalendarSectionDto calendar =
                        calendarService.getCalendar(user.getUserIndex(), new CalendarGetRequestDto(time.getMonthValue(),
                                                                                                    time.getYear()));
            }

            Project project;
            if(isStudy) {
                if(requestDto.getStudyIdx() == null)
                    throw new EmptyInputException(EMPTY_STUDY);
                Study study = findStudyByIdx(requestDto.getStudyIdx());
                project = new Project(requestDto, user, study, studyNum);
                study.addProject(project);
            } else {
                project = new Project(requestDto, user);
            }

            user.addProject(project);
            return projectRepository.save(project);
        } catch (EmptyEntityException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public Project updateProjectIsVisible(Long projectIdx) throws BaseException {
        try{
            Project project = findProjectByIdx(projectIdx);
            User user = project.getUser();

            if(project.getProjectIsVisible()) {
                project.updateProjectIsVisible(false);
            } else {
                Calendar calendar = findCalendarByUserIdxAndDate(user.getUserIndex(),
                        project.getProjectStartTime().getYear(),
                        project.getProjectStartTime().getMonthValue());
                List<Planner> plannerList = calendar.getCalendarPlanners();
                // 플래너 isVisble 5개 넘는지 체크
                for (LocalDate date = project.getProjectStartTime(); date.isBefore(project.getProjectEndTime().plusDays(1)); date = date.plusDays(1)) {
                    // 달 지나면 새로운 캘린더 가져오기
                    if (date.getMonthValue() != calendar.getCalendarMonth()) {
                        calendar = findCalendarByUserIdxAndDate(user.getUserIndex(), date.getYear(), date.getMonthValue());
                        plannerList = calendar.getCalendarPlanners();
                    }

                    // 프로젝트 일정을 걸친 날들의 is visible == true 플랜 + 프로젝트 개수 세기
                    for (Planner planner : plannerList) {
                        if (planner.getPlannerDate().equals(date)) {
                            int count = 0;
                            for (Plan plannerPlan : planner.getPlannerPlans()) {
                                if (plannerPlan.getPlanIsVisible()) {
                                    count++;
                                }
                                if (count >= 5) {
                                    throw new OutOfRangeException(PROJECT_FULL_VISIBLE);
                                }
                            }
                            for (Project userProject : user.getUserProjects()) {
                                if ((userProject.getProjectStartTime().isBefore(date) || userProject.getProjectStartTime().isEqual(date)) &&
                                        (userProject.getProjectEndTime().isAfter(date) || userProject.getProjectEndTime().isEqual(date))) {
                                    if (userProject.getProjectIsVisible()) {
                                        count++;
                                    }
                                    if (count >= 5) {
                                        throw new OutOfRangeException(PROJECT_FULL_VISIBLE);
                                    }
                                }
                            }
                            break;
                        }
                    }
                }

                project.updateProjectIsVisible(true);
            }
            return project;

        } catch (EmptyEntityException |
                 OutOfRangeException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public Project updateProjectIsComplete(Long projectIdx) throws BaseException {
        try {
            Project project = findProjectByIdx(projectIdx);
            project.updateProjectIsComplete(!project.getProjectIsComplete());

            return project;
        } catch (EmptyEntityException e) {
            throw new BaseException(e.getStatus());
        } catch (BaseException e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 스터디라면 스터디매핑가능하도록 + 권한
    @Transactional
    public Project updateProject(Long projectIdx, ProjectUpdateRequestDto requestDto, Boolean isStudyPage, Long adminUserStudyIdx) throws BaseException {
        try {
            Project project = findProjectByIdx(projectIdx);

            // 스터디 프로젝트는 스터디에서만 수정이 가능하도록
            if(isStudyPage) {
                if(!project.getProjectIsStudy())
                    throw new BusinessLogicException(BUSINESS_LOGIC_ERROR);
                if(findUserstudyByIdx(adminUserStudyIdx).getUserstudyMemberStatus().equals(1))
                    throw new NotAuthorizedAccessException(NOT_AUTHORIZED);
            } else
                if(project.getProjectIsStudy())
                    throw new BusinessLogicException(BUSINESS_LOGIC_ERROR);

            if(requestDto.getNewStartTime() != null &&
                    requestDto.getNewEndTime() != null &&
                    requestDto.getNewStartTime().isAfter(requestDto.getNewEndTime()))
                throw new BusinessLogicException(BUSINESS_LOGIC_ERROR);

            // 시간도 수정할 수 있도록 수정

            if(isStudyPage) {
                for (Project studyProject : project.getStudy().getStudyProjects()) {
                    if(studyProject.getProjectNumber().equals(project.getProjectNumber())) {
                        if(!requestDto.getNewName().isEmpty()) {
                            project.updateProjectName(requestDto.getNewName());
                        }
                        if(requestDto.getNewEndTime() != null) {
                            if(project.getProjectStartTime().isAfter(requestDto.getNewEndTime()))
                                throw new BusinessLogicException(BUSINESS_LOGIC_ERROR);
                            project.updateProjectEndTime(requestDto.getNewEndTime());
                        } if(requestDto.getNewStartTime() != null) {
                            if(requestDto.getNewStartTime().isAfter(project.getProjectEndTime()))
                                throw new BusinessLogicException(BUSINESS_LOGIC_ERROR);
                            project.updateProjectStartTime(requestDto.getNewStartTime());
                        }
                    }
                }
            } else {
                if(!requestDto.getNewName().isEmpty())
                    project.updateProjectName(requestDto.getNewName());
                if(requestDto.getNewEndTime() != null) {
                    if(project.getProjectStartTime().isAfter(requestDto.getNewEndTime()))
                        throw new BusinessLogicException(BUSINESS_LOGIC_ERROR);
                    project.updateProjectEndTime(requestDto.getNewEndTime());
                }
                if(requestDto.getNewStartTime() != null) {
                    if(requestDto.getNewStartTime().isAfter(project.getProjectEndTime()))
                        throw new BusinessLogicException(BUSINESS_LOGIC_ERROR);
                    project.updateProjectStartTime(requestDto.getNewStartTime());
                }

            }


            return project;
        } catch (EmptyEntityException |
                 BusinessLogicException e) {
            throw new BaseException(e.getStatus());
        } catch (BaseException e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    @Transactional
    public void deleteProject(Long projectIdx) throws BaseException {
        try {
            Project project = findProjectByIdx(projectIdx);
            projectRepository.delete(project);
        } catch (EmptyEntityException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public List<Project> getStudyProjects(Long userIdx) throws BaseException {
        try {
            List<Project> projects = new ArrayList<>();

            User user = findUserByIdx(userIdx);
            for (UserStudy userstudy : user.getUserUserstudies()) {
                projects.addAll(userstudy.getStudy().getStudyProjects());
            }

            return projects;
        } catch (EmptyEntityException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
