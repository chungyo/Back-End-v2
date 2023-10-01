package com.mmos.mmos.src.repository;

import com.mmos.mmos.src.domain.entity.College;
import com.mmos.mmos.src.domain.entity.University;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CollegeRepository extends JpaRepository<College, Long> {
    Optional<List<College>> findAllByUniversity(University university);
}

