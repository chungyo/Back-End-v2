package com.mmos.mmos.src.repository;

import com.mmos.mmos.src.domain.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project,Long> {
}
