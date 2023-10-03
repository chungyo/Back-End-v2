package com.mmos.mmos.src.repository;

import com.mmos.mmos.src.domain.entity.Streak;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StreakRepository extends JpaRepository<Streak, Long> {
}
