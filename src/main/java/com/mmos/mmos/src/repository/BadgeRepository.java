package com.mmos.mmos.src.repository;

import com.mmos.mmos.src.domain.entity.Badge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BadgeRepository extends JpaRepository<Badge, Long> {

    Optional<List<Badge>> findBadgesByBadgePurpose(String purpose);
}