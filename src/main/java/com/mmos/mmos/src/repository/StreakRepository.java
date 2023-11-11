package com.mmos.mmos.src.repository;

import com.mmos.mmos.src.domain.entity.Streak;
import com.mmos.mmos.src.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StreakRepository extends JpaRepository<Streak, Long> {
    Optional<List<Streak>> findTop60ByUserOrderByStreakIndexDesc(User user);
}
