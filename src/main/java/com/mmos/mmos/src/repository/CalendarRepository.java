package com.mmos.mmos.src.repository;

import com.mmos.mmos.src.domain.entity.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {
}