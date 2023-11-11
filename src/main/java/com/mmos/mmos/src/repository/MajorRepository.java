package com.mmos.mmos.src.repository;

import com.mmos.mmos.src.domain.entity.College;
import com.mmos.mmos.src.domain.entity.Major;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MajorRepository extends JpaRepository<Major, Long> {
    Optional<List<Major>> findAllByCollege(College college);
}
