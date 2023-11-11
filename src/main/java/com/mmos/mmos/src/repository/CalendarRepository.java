package com.mmos.mmos.src.repository;

import com.mmos.mmos.src.domain.entity.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    Optional<Calendar> findCalendarByUser_UserIndexAndCalendarYearAndCalendarMonth(Long userIdx,int year, Integer month);
}