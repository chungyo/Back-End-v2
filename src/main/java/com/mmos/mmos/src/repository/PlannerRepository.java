package com.mmos.mmos.src.repository;

import com.mmos.mmos.src.domain.entity.Planner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface PlannerRepository extends JpaRepository<Planner, Long> {
    Optional<Planner> findPlannerByCalendar_CalendarIndexAndPlannerDate(Long idx, LocalDate date);
}
