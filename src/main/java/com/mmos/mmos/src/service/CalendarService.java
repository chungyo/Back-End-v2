package com.mmos.mmos.src.service;

import com.mmos.mmos.config.exception.BaseException;
import com.mmos.mmos.config.exception.DuplicateRequestException;
import com.mmos.mmos.config.exception.EmptyEntityException;
import com.mmos.mmos.src.domain.dto.request.CalendarGetRequestDto;
import com.mmos.mmos.src.domain.dto.response.home.CalendarSectionDto;
import com.mmos.mmos.src.domain.entity.Calendar;
import com.mmos.mmos.src.domain.entity.Project;
import com.mmos.mmos.src.domain.entity.User;
import com.mmos.mmos.src.repository.CalendarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.mmos.mmos.config.HttpResponseStatus.*;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final CalendarRepository calendarRepository;
    private final UserService userService;

    public Calendar findCalendarByMonthAndYear(Long userIdx, Integer calendarYear, Integer calendarMonth) throws BaseException {
        return calendarRepository.findCalendarByUser_UserIndexAndCalendarYearAndCalendarMonth(userIdx, calendarYear, calendarMonth)
                .orElseThrow(() -> new EmptyEntityException(EMPTY_CALENDAR));
    }

    public Calendar findCalendarByIdx(Long calendarIdx) throws BaseException {
        return calendarRepository.findById(calendarIdx)
                .orElseThrow(() -> new EmptyEntityException(EMPTY_CALENDAR));
    }

    // 캘린더 생성
    @Transactional
    public Calendar saveCalendar(Integer year, Integer month, Long userIdx) throws BaseException {
        try {
            User user = userService.getUser(userIdx);

            // 막 회원 가입을 한 유저가 아니면서 같은 달의 캘린더가 이미 존재할 때 생성 막기
            if (!user.getUserCalendars().isEmpty()) {
                boolean isExist = false;
                for (Calendar userCalendar : user.getUserCalendars()) {
                    if (userCalendar.getCalendarYear().equals(year) && userCalendar.getCalendarMonth().equals(month)) {
                        isExist = true;
                        break;
                    }
                }

                if (isExist) {
                    throw new DuplicateRequestException(CALENDAR_INVALID_REQUEST);
                }
            }

            Calendar calendar = new Calendar(year, month, user);
            // User, Calendar 양방향 매핑
            user.addCalendars(calendar);

            calendarRepository.save(calendar);

            return calendar;
        } catch (EmptyEntityException |
                 DuplicateRequestException e) {
          throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public CalendarSectionDto getCalendar(Long userIdx, CalendarGetRequestDto calendarGetRequestDto) throws BaseException {
        // User 가져오기
        try {
            User user = userService.getUser(userIdx);

            // Calendar 가져오기
            boolean isExist = false;
            Calendar calendar = null;
            for (Calendar userCalendar : user.getUserCalendars()) {
                if (userCalendar.getCalendarYear().equals(calendarGetRequestDto.getYear()) && userCalendar.getCalendarMonth().equals(calendarGetRequestDto.getMonth())) {
                    calendar = userCalendar;
                    isExist = true;
                }
            }
            if (!isExist) {
                saveCalendar(calendarGetRequestDto.getYear(), calendarGetRequestDto.getMonth(), userIdx);
                calendar = findCalendarByMonthAndYear(userIdx, calendarGetRequestDto.getYear(), calendarGetRequestDto.getMonth());
            }

            List<Project> userProjectList = user.getUserProjects();
            List<Project> calendarProjectList = new ArrayList<>();

            if(isExist) {
                // 해당 달을 포함하는 Project 찾기
                LocalDate startDate;
                LocalDate endDate;
                LocalDate calendarDate = LocalDate.of(calendarGetRequestDto.getYear(), calendarGetRequestDto.getMonth(), 1);

                for (Project project : userProjectList) {
                    startDate = LocalDate.of(project.getProjectStartTime().getYear(), project.getProjectStartTime().getMonthValue(), 1);
                    endDate = LocalDate.of(project.getProjectEndTime().getYear(), project.getProjectEndTime().getMonthValue(), 20);
                    if ((startDate.isBefore(calendarDate) || startDate.isEqual(calendarDate)) && (endDate.isAfter(calendarDate) || endDate.isEqual((calendarDate)))) {
                        calendarProjectList.add(project);
                    }
                }
            }

            return new CalendarSectionDto(calendar, calendarProjectList);
        } catch (EmptyEntityException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
