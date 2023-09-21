package com.mmos.mmos.src.service;

import com.mmos.mmos.src.domain.dto.calendar.CalendarResponseDto;
import com.mmos.mmos.src.domain.entity.Calendar;
import com.mmos.mmos.src.domain.entity.User;
import com.mmos.mmos.src.repository.CalendarRepository;
import com.mmos.mmos.src.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final CalendarRepository calendarRepository;
    private final UserRepository userRepository;

    public User findUser(Long userIdx) {
        return userRepository.findById(userIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다. USER_INDEX=" + userIdx));
    }

    // 캘린더 생성
    @Transactional
    public CalendarResponseDto saveCalendar(int month, Long userIdx) {
        User user = findUser(userIdx);

        // 막 회원 가입을 한 유저가 아니면서 같은 달의 캘린더가 이미 존재할 때 생성 막기
        if(!user.getUserCalendars().isEmpty() && user.getUserCalendars().get(user.getUserCalendars().size() - 1).getCalendarMonth() == month)
            return null;

        Calendar calendar = new Calendar(month, user);
        // User, Calendar 양방향 매핑
        user.addCalendars(calendar);

        calendarRepository.save(calendar);

        return new CalendarResponseDto(calendar);
    }
}
